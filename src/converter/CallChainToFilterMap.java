package converter;

import exceptions.ArrayOperationsException;
import expression.Expression;
import expression.boolean_operators.BooleanOperator;
import expression.boolean_operators.comparison.Equal;
import expression.boolean_operators.logic.And;
import expression.polynomial.Polynomial;

import java.util.List;

import static expression.boolean_operators.comparison.Equal.TRUE;

public class CallChainToFilterMap {
    static public String convert(List<Expression> callChains) {
        Polynomial map = new Polynomial(1, 1);
        BooleanOperator filter = null;
        for (Expression expression : callChains) {
            if (expression instanceof Polynomial) {
                map = ((Polynomial) expression).compose(map);
            } else if (expression instanceof BooleanOperator) {
                BooleanOperator booleanOperator = (BooleanOperator) expression;
                filter = (filter == null) ? booleanOperator : new And(filter, booleanOperator);
            } else {
                throw new ArrayOperationsException("Invalid type");
            }
        }
        if (filter == null) {
            filter = TRUE;
        }
        return String.format("filter{%s}%%>%%map{%s}", filter.toString(), map.toString());
    }
}