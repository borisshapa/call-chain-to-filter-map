package expression.boolean_operators.comparison;

import expression.Expression;
import expression.boolean_operators.BooleanOperator;
import expression.polynomial.Polynomial;

import java.util.Map;

public class ComparisonOperator extends BooleanOperator {
    public ComparisonOperator(Polynomial arg1, Polynomial arg2, String operator) {
        super(arg1, arg2, operator);
    }
}
