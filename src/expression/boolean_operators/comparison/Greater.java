package expression.boolean_operators.comparison;

import expression.Expression;
import expression.boolean_operators.BooleanOperator;
import expression.polynomial.Polynomial;
import set.Bound;
import set.RealNumbersSet;
import set.Segment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Greater extends ComparisonOperator {
    @Override
    public RealNumbersSet getValuesSet() {
        return getInequalityValuesSet(false);
    }

    public Greater(Polynomial arg1, Polynomial arg2) {
        super(arg1, arg2, ">");
    }

    @Override
    public Expression compose(Polynomial polynomial) {
        return new Greater(((Polynomial) arg1).compose(polynomial), ((Polynomial) arg2).compose(polynomial));
    }
}
