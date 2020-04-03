package expression.boolean_operators.comparison;

import expression.Expression;
import expression.polynomial.Polynomial;
import set.RealNumbersSet;

public class Less extends ComparisonOperator {
    @Override
    public RealNumbersSet getValuesSet() {
        return getInequalityValuesSet(true);
    }

    public Less(Polynomial arg1, Polynomial arg2) {
        super(arg1, arg2, "<");
    }

    @Override
    public Expression compose(Polynomial polynomial) {
        return new Less(((Polynomial) arg1).compose(polynomial), ((Polynomial) arg2).compose(polynomial));
    }
}
