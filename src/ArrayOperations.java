import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class ArrayOperations {
    private static final Map<Character, BinaryOperator<Integer>> INTEGER_OPERATORS = Map.of(
            '+', Integer::sum,
            '-', (a, b) -> a - b,
            '*', (a, b) -> a * b
    );

    private static final Map<Character, BinaryOperator<Boolean>> BOOLEAN_OPERATORS = Map.of(
            '&', (a, b) -> a && b,
            '|', (a, b) -> a || b,
            '=', (a, b) -> a == b
    );

    private static final Map<Character, BiFunction<Integer, Integer, Boolean>> COMPARISON_OPERATORS = Map.of(
            '=', Integer::equals,
            '<', (a, b) -> a < b,
            '>', (a, b) -> a > b
    );

    public static Object parse(final String source) {
        return parse(new StringSource(source));
    }

    public static Object parse(ArrayOperationsSource source) {
        return new ArrayOperationsParser(source).parseCallChain();
    }

    private static class ArrayOperationsParser extends BaseParser {
        public ArrayOperationsParser(final ArrayOperationsSource source) {
            super(source);
            nextChar();
        }

        /*
         * call-chain
         *      call
         *      call '%>%' call-chain
         */
        private List<Object> parseCallChain() {
            List<Object> array = new ArrayList<>();
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
        private Object parseCall() {
            return null;
        }

        private Object parseMapCall() {
            return null;
        }

        private Object parseFilterCall() {
            return null;
        }

        private Object parseExpression() {
            return null;
        }

        private Object parseBinaryExpression() {
            return null;
        }


        private Object parseOperation() {
            return null;
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
        private Object parseConstantExpression() {
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
            return Integer.parseInt(sb.toString());
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
