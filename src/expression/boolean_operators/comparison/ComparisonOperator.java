package expression.boolean_operators.comparison;

import expression.Expression;
import expression.boolean_operators.BooleanOperator;
import expression.polynomial.Polynomial;
import set.Bound;
import set.RealNumbersSet;

import java.util.ArrayList;
import java.util.List;

public abstract class ComparisonOperator extends BooleanOperator {
    protected RealNumbersSet getInequalityValuesSet(Boolean type) {
        Polynomial diff = ((Polynomial) arg1).subtract((Polynomial) arg2);
        List<Double> bounds = new ArrayList<>();
        bounds.add(Double.NEGATIVE_INFINITY);
        bounds.addAll(diff.getRoots());
        bounds.add(Double.POSITIVE_INFINITY);

        int firstSegment = ((diff.signAtNegativeInfinity() > 0) ^ type) ? 0 : 1;
        RealNumbersSet result = new RealNumbersSet();
        for (int i = firstSegment; i < bounds.size() - 1; i += 2) {
            Double boundL = bounds.get(i);
            Double boundR = bounds.get(i + 1);
            Bound lb = new Bound(isInfinityOrInteger(boundL) ? boundL : Math.floor(boundL), false);
            Bound rb = new Bound(isInfinityOrInteger(boundR) ? boundR : Math.ceil(boundR), false);
            result = result.union(new RealNumbersSet(lb, rb));
        }
        return result;
    }

    protected static boolean isInteger(Double x) {
        return (!Double.isInfinite(x) && x == Math.floor(x));
    }

    protected static boolean isInfinityOrInteger(Double x) {
        return Double.isInfinite(x) || isInteger(x);
    }

    public ComparisonOperator(Polynomial arg1, Polynomial arg2, String operator) {
        super(arg1, arg2, operator);
    }
}
