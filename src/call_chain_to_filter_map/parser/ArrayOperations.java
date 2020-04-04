package call_chain_to_filter_map.parser;

import call_chain_to_filter_map.expression.Expression;
import call_chain_to_filter_map.expression.boolean_operators.BooleanOperator;
import call_chain_to_filter_map.expression.boolean_operators.comparison.Equal;
import call_chain_to_filter_map.expression.boolean_operators.comparison.Greater;
import call_chain_to_filter_map.expression.boolean_operators.comparison.Less;
import call_chain_to_filter_map.expression.boolean_operators.logic.And;
import call_chain_to_filter_map.expression.boolean_operators.logic.Or;
import call_chain_to_filter_map.expression.polynomial.Polynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * Converts an array operations string to a {@link List} of {@link Expression}.
 * The sequence of operations is given by the following grammar.
 * <pre> {@code
 * <ws> ::= "" | '0020' | '000A' | '000D' | '0009'
 * <ws-seq> ::= <ws> | <ws> <ws-seq>
 * <digit> ::= “0” | “1" | “2” | “3" | “4” | “5" | “6” | “7" | “8” | “9"
 * <number> ::= <digit> | <digit> <number>
 * <operation> ::= “+” | “-” | “*” | “>” | “<” | “=” | “&” | “|”
 * <constant-expression> ::= “-” <number> | <number>
 * <binary-expression> ::= “(” <expression> <operation> <expression> “)”
 * <weak-expression> ::= “element” | <constant-expression> | <binary-expression>
 * <expression> ::= <ws-seq> <weak-expression> <ws-seq>
 * <map-call> ::= “map{” <expression> “}”
 * <filter-call> ::= “filter{” <expression> “}”
 * <weak-call> ::= <map-call> | <filter-call>
 * <call> ::= <ws-seq> <weak-call> <ws-seq>
 * <call-chain> ::= <call> | <call> “%>%” <call-chain>}
 * </pre>
 *
 * The operation “&” is a logical “and”, the operation “|” - logical “or“.
 * Binary expressions with operators “&”, “|” , “=”, “>”, “<” are of Boolean type,
 * and with the operators “+”, “-”, “*” they are arithmetic.
 * The operands of arithmetic operations must be of integer type,
 * and the operands of logical operations must be Boolean.
 * The call to the map function replaces each element of the array with the result of calculating
 * the passed arithmetic expression, in which the value of the current element is substituted for element.
 * Calling the filter function leaves in the array only elements for which the expression passed is true.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class ArrayOperations {
    private static final Map<Character, BinaryOperator<Polynomial>> INTEGER_OPERATORS = Map.of(
            '+', Polynomial::add,
            '-', Polynomial::subtract,
            '*', Polynomial::multiply
    );

    private static final Map<Character, BinaryOperator<BooleanOperator>> LOGIC_OPERATORS = Map.of(
            '&', And::new,
            '|', Or::new
    );

    private static final Map<Character, BiFunction<Polynomial, Polynomial,
            BooleanOperator>> COMPARISON_OPERATORS = Map.of(
            '=', Equal::new,
            '<', Less::new,
            '>', Greater::new
    );

    /**
     * Parses {@code <call-chain>} {@link String}.
     *
     * @param source {@code <call-chain>} {@link String}
     * @return {@link List} of {@link Expression} where {@link Polynomial} denotes 'map' from source string,
     * {@link BooleanOperator} denotes 'filter'
     */
    public static List<Expression> parse(final String source) {
        return parse(new StringSource(source));
    }

    /**
     * Parses {@code <call-chain>} {@link ArrayOperationsSource}.
     *
     * @param source {@code <call-chain>} {@link ArrayOperationsSource}
     * @return {@link List} of {@link Expression} where {@link Polynomial} denotes 'map' from source string,
     * {@link BooleanOperator} denotes 'filter'
     */
    public static List<Expression> parse(ArrayOperationsSource source) {
        return new ArrayOperationsParser(source).parseArrayOperations();
    }

    private static class ArrayOperationsParser extends BaseParser {
        public ArrayOperationsParser(final ArrayOperationsSource source) {
            super(source);
            nextChar();
        }

        /*
         * array-operations
         *      call-chain
         */
        public List<Expression> parseArrayOperations() {
            final List<Expression> result = parseCallChain();
            if (test(ArrayOperationsSource.END)) {
                return result;
            }
            throw syntaxError("End of chain calls expected");
        }

        /*
         * call-chain
         *      call
         *      call '%>%' call-chain
         */
        private List<Expression> parseCallChain() {
            List<Expression> array = new ArrayList<>();
            array.add(parseCall());
            while (test('%')) {
                expect(">%");
                array.add(parseCall());
            }
            return array;
        }

        /*
         * weak-call
         *      map-call
         *      filter-call
         *
         * call
         *      ws weak-call ws
         */
        private Expression parseCall() {
            Expression result;
            skipWhitespace();
            if (test('f')) {
                expect("ilter{");
                result = parseExpression();
                expectType(result, BooleanOperator.class);
            } else if (test('m')) {
                expect("ap{");
                result = parseExpression();
                expectType(result, Polynomial.class);
            } else {
                throw syntaxError("Invalid array operation");
            }
            expect('}');
            skipWhitespace();
            return result;
        }

        /*
         * weak-expression
         *      "element"
         *      constant-expression
         *      binary-expression
         *
         * expression
         *      ws weak-expression ws
         */
        private Expression parseExpression() {
            skipWhitespace();
            Expression result;
            if (test('e')) {
                expect("lement");
                result = new Polynomial(1, 1);
            } else if (between('0', '9') || ch == '-') {
                result = parseConstantExpression();
            } else {
                result = parseBinaryExpression();
            }
            skipWhitespace();
            return result;
        }

        /*
         * operation
         *      '+'
         *      '-'
         *      '*'
         *      '>'
         *      '<'
         *      '='
         *      '&'
         *      '|'
         * binary-expression
         *      '(' expression operation expression ')'
         */
        private Expression parseBinaryExpression() {
            expect('(');
            Expression leftOperand = parseExpression();
            char operator = ch;
            nextChar();
            Expression rightOperand = parseExpression();
            expect(')');
            if (leftOperand instanceof Polynomial && rightOperand instanceof Polynomial) {
                Polynomial polynomial1 = (Polynomial) leftOperand;
                Polynomial polynomial2 = (Polynomial) rightOperand;

                if (INTEGER_OPERATORS.containsKey(operator)) {
                    return INTEGER_OPERATORS.get(operator).apply(polynomial1, polynomial2);
                } else if (COMPARISON_OPERATORS.containsKey(operator)) {
                    return COMPARISON_OPERATORS.get(operator).apply(polynomial1, polynomial2);
                } else {
                    if (LOGIC_OPERATORS.containsKey(operator)) {
                        throw typeError("Logical operations take boolean arguments");
                    } else {
                        throw syntaxError("Invalid operation");
                    }
                }
            } else if (leftOperand instanceof BooleanOperator && rightOperand instanceof BooleanOperator) {
                BooleanOperator boolean1 = (BooleanOperator) leftOperand;
                BooleanOperator boolean2 = (BooleanOperator) rightOperand;

                if (LOGIC_OPERATORS.containsKey(operator)) {
                    return LOGIC_OPERATORS.get(operator).apply(boolean1, boolean2);
                } else {
                    if (INTEGER_OPERATORS.containsKey(operator) || COMPARISON_OPERATORS.containsKey(operator)) {
                        throw typeError("Arithmetic and comparison operations accept polynomials as arguments");
                    } else {
                        throw syntaxError("Invalid operation");
                    }
                }
            } else {
                throw typeError("Arguments must be of the same type.");
            }
        }

        /*
         * digit
         *      '0' . '9'
         *
         * number
         *      number
         *      digit number
         *
         * constant-expression
         *      number
         *      '-' number
         */
        private Polynomial parseConstantExpression() {
            final StringBuilder sb = new StringBuilder();
            if (test('-')) {
                sb.append('-');
            }
            if (between('0', '9')) {
                while (between('0', '9')) {
                    sb.append(ch);
                    nextChar();
                }
            } else {
                throw syntaxError("Invalid number");
            }
            return new Polynomial(Integer.parseInt(sb.toString()), 0);
        }


        /*
         * ws-seq
         *    ""
         *    '0020' ws-seq
         *    '000A' ws-seq
         *    '000D' ws-seq
         *    '0009' ws-seq
         */
        private void skipWhitespace() {
            while (test(' ') || test('\r') || test('\n') || test('\t')) {
                // skip
            }
        }
    }
}
