package converter;

import exceptions.ArrayOperationsException;
import expression.Expression;
import expression.boolean_operators.BooleanOperator;
import expression.boolean_operators.comparison.Equal;
import expression.boolean_operators.logic.And;
import expression.polynomial.Polynomial;
import set.Bound;
import set.RealNumbersSet;
import set.Segment;

import java.util.List;

public class CallChainToFilterMap {

    private static String getTerm(Segment segment) {
        Bound lb = segment.getLowerBound();
        Bound ub = segment.getUpperBound();

        if (lb.compareTo(ub) == 0) {
            return (lb.inclusive && ub.inclusive)
                    ? String.format("(%s=%d)", Polynomial.VARIABLE_NAME, lb.getValue().intValue())
                    : "";
        }

        if (lb.getValue() == Double.NEGATIVE_INFINITY && ub.getValue() == Double.POSITIVE_INFINITY) {
            return "(0=0)";
        }
        int lbInt = lb.getValue().intValue();
        int ubInt = ub.getValue().intValue();

        String lbStr = String.format("(%s>%d)", Polynomial.VARIABLE_NAME, lbInt);
        String ubStr = String.format("(%s<%d)", Polynomial.VARIABLE_NAME, ubInt);

        String lbPoint = String.format("(%s=%d)", Polynomial.VARIABLE_NAME, lbInt);
        String ubPoint = String.format("(%s=%d)", Polynomial.VARIABLE_NAME, ubInt);

        String result = (lb.getValue() == Double.NEGATIVE_INFINITY)
                ? ubStr : (ub.getValue() == Double.POSITIVE_INFINITY)
                ? lbStr : String.format("(%s&%s)", lbStr, ubStr);

        if (lb.inclusive) {
            result = String.format("(%s|%s)", lbPoint, result);
        }
        if (ub.inclusive) {
            result = String.format("(%s|%s)", result, ubPoint);
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
                    sb.append(String.format("&%s)", term));
                    openingBrackets++;
                }
            }
        }
        String result = sb.toString();
        return result.isEmpty() ? "(1=0)" : "(".repeat(openingBrackets) + sb.toString();
    }

    static public String convert(List<Expression> callChains) {
        Polynomial map = new Polynomial(1, 1);
        RealNumbersSet filter = new RealNumbersSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        for (Expression expression : callChains) {
            if (expression instanceof Polynomial) {
                map = ((Polynomial) expression).compose(map);
            } else if (expression instanceof BooleanOperator) {
                BooleanOperator bo = (BooleanOperator) expression.compose(map);
                filter = filter.intersection(((BooleanOperator) expression.compose(map)).getValuesSet());
            } else {
                throw new ArrayOperationsException("Invalid type");
            }
        }

        return String.format("filter{%s}%%>%%map{%s}", setToString(filter.getSegments()), map.toString());
    }
}