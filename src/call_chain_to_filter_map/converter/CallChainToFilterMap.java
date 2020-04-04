package call_chain_to_filter_map.converter;

import call_chain_to_filter_map.expression.Expression;
import call_chain_to_filter_map.expression.boolean_operators.BooleanOperator;
import call_chain_to_filter_map.expression.polynomial.Polynomial;
import call_chain_to_filter_map.parser.exceptions.UnsupportedTypeException;
import call_chain_to_filter_map.set.Bound;
import call_chain_to_filter_map.set.RealNumbersSet;
import call_chain_to_filter_map.set.Segment;

import java.util.List;

/**
 * Function call converter to {@code <filter>%>%<map>} form.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class CallChainToFilterMap {
    /**
     * A filter denoting the set of all numbers.
     */
    public static final String TRUE = "(0=0)";

    /**
     * A filter denoting the empty set of numbers.
     */
    public static final String FALSE = "(1=0)";

    private static String getTerm(Segment segment) {
        Bound lb = segment.getLowerBound();
        Bound ub = segment.getUpperBound();

        if (lb.compareTo(ub) == 0) {
            return (lb.inclusive && ub.inclusive)
                    ? String.format("(%s=%d)", Polynomial.VARIABLE_NAME, lb.getValue().intValue())
                    : "";
        }

        if (lb.getValue() == Double.NEGATIVE_INFINITY && ub.getValue() == Double.POSITIVE_INFINITY) {
            return TRUE;
        }
        int lbInt = lb.getValue().intValue();
        int ubInt = ub.getValue().intValue();

        String lbStr = String.format("(%s>%d)", Polynomial.VARIABLE_NAME, lbInt);
        String ubStr = String.format("(%s<%d)", Polynomial.VARIABLE_NAME, ubInt);

        String lbPoint = String.format("(%s=%d)", Polynomial.VARIABLE_NAME, lbInt);
        String ubPoint = String.format("(%s=%d)", Polynomial.VARIABLE_NAME, ubInt);

        String result = (lb.getValue() == Double.NEGATIVE_INFINITY) ? ubStr
                : (ub.getValue() == Double.POSITIVE_INFINITY) ? lbStr
                : (lbInt == ubInt - 2) ? String.format("(%s=%d)", Polynomial.VARIABLE_NAME, lbInt + 1)
                : (lbInt == ubInt - 1) ? ""
                : String.format("(%s&%s)", lbStr, ubStr);

        if (lb.inclusive) {
            result = result.isEmpty() ? lbPoint : String.format("(%s|%s)", lbPoint, result);
        }
        if (ub.inclusive) {
            result = result.isEmpty() ? ubPoint : String.format("(%s|%s)", result, ubPoint);
        }
        return result;
    }

    private static String setToString(List<Segment> segments) {
        StringBuilder sb = new StringBuilder();
        int openingBrackets = 0;
        for (Segment segment : segments) {
            String term = getTerm(segment);
            if (!term.isEmpty()) {
                if (sb.toString().isEmpty()) {
                    sb.append(term);
                } else {
                    sb.append(String.format("|%s)", term));
                    openingBrackets++;
                }
            }
        }
        String result = sb.toString();
        return result.isEmpty() ? FALSE : "(".repeat(openingBrackets) + sb.toString();
    }

    /**
     * According to the {@link List} of {@link Expression}, returns a {@link String} of the form "filter{%s}%>%map{%s}".
     * An {@link Expression} of type {@link Polynomial} denotes an <var>map</var> operation on array elements,
     * {@link BooleanOperator} denotes <var>filter</var>.
     *
     * @param callChains {@link List} of array operations
     * @return {@link String} of the form "filter{%s}%>%map{%s}"
     */
    static public String convert(List<Expression> callChains) {
        Polynomial map = new Polynomial(1, 1);
        RealNumbersSet filter = new RealNumbersSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        for (Expression expression : callChains) {
            if (expression instanceof Polynomial) {
                map = ((Polynomial) expression).compose(map);
            } else if (expression instanceof BooleanOperator) {
                filter = filter.intersection(((BooleanOperator) expression.compose(map)).getValuesSet());
            } else {
                throw new UnsupportedTypeException("Expected call_chain_to_filter_map.expression.polynomial.Polynomial for map operation or call_chain_to_filter_map.expression.boolean_operators.BooleanOperator for filter operation");
            }
        }

        String filterStr = setToString(filter.getSegments());
        String mapStr = (filterStr.equals(FALSE)) ? Polynomial.VARIABLE_NAME : map.toString();
        return String.format("filter{%s}%%>%%map{%s}", filterStr, mapStr);
    }
}