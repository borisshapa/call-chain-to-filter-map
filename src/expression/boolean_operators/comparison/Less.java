package expression.boolean_operators.comparison;

import expression.polynomial.Polynomial;

public class Less extends ComparisonOperator {
    public Less(Polynomial arg1, Polynomial arg2) {
        super(arg1, arg2, "<");
    }
}
