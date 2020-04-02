package parser;

import expression.Expression;
import expression.boolean_operators.BooleanOperator;
import expression.boolean_operators.comparison.Greater;
import expression.boolean_operators.comparison.Less;
import expression.boolean_operators.logic.And;
import expression.boolean_operators.logic.Or;
import expression.polynomial.Polynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
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
            '|', Or::new,
            '=', expression.boolean_operators.logic.Equal::new
    );

    private static final Map<Character, BiFunction<Polynomial, Polynomial,
            BooleanOperator>> COMPARISON_OPERATORS = Map.of(
            '=', expression.boolean_operators.comparison.Equal::new,
            '<', Less::new,
            '>', Greater::new
    );

    public static List<Expression> parse(final String source) {
        return parse(new StringSource(source));
    }

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
            throw error("End of chain calls expected");
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
         * call
         *      map-call
         *      filter-call
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
                throw error("Invalid array operation");
            }
            expect('}');
            skipWhitespace();
            return result;
        }

        /*
         * expression
         *      "element"
         *      constant-expression
         *      binary-expression
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
            Expression result;
            if (leftOperand instanceof Polynomial && rightOperand instanceof Polynomial) {
                Polynomial polynomial1 = (Polynomial) leftOperand;
                Polynomial polynomial2 = (Polynomial) rightOperand;

                if (INTEGER_OPERATORS.containsKey(operator)) {
                    result = INTEGER_OPERATORS.get(operator).apply(polynomial1, polynomial2);
                } else if (COMPARISON_OPERATORS.containsKey(operator)) {
                    result = COMPARISON_OPERATORS.get(operator).apply(polynomial1, polynomial2);
                } else {
                    throw error("Invalid expression type or operand");
                }
            } else if (leftOperand instanceof BooleanOperator && rightOperand instanceof BooleanOperator) {
                BooleanOperator boolean1 = (BooleanOperator) leftOperand;
                BooleanOperator boolean2 = (BooleanOperator) rightOperand;

                if (LOGIC_OPERATORS.containsKey(operator)) {
                    result = LOGIC_OPERATORS.get(operator).apply(boolean1, boolean2);
                } else {
                    throw error("Invalid expression type or operand");
                }
            } else {
                throw error("Invalid expression type");
            }
            expect(')');
            return result;
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
                throw error("Invalid number");
            }
            return new Polynomial(Integer.parseInt(sb.toString()), 0);
        }


        /*
         * ws
         *    ""
         *    '0020' ws
         *    '000A' ws
         *    '000D' ws
         *    '0009' ws
         */
        private void skipWhitespace() {
            while (test(' ') || test('\r') || test('\n') || test('\t')) {
                // skip
            }
        }
    }
}
