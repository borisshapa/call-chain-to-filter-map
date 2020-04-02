package expression.boolean_operators.comparison;

import expression.boolean_operators.BooleanOperator;
import expression.polynomial.Polynomial;

public class Equal extends ComparisonOperator {
    private final static Polynomial ONE = new Polynomial(1, 0);
    private final static  Polynomial ZERO = new Polynomial(0, 0);

    public final static BooleanOperator TRUE = new Equal(ZERO, ZERO);
    public final static BooleanOperator FALSE = new Equal(ONE, ZERO);

    public Equal(Polynomial arg1, Polynomial arg2) {
        super(arg1, arg2, "=");
    }
}
