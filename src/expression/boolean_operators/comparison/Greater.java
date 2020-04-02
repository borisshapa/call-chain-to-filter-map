package expression.boolean_operators.comparison;

import expression.polynomial.Polynomial;

public class Greater extends ComparisonOperator {
    public Greater(Polynomial arg1, Polynomial arg2) {
        super(arg1, arg2, ">");
    }
}
