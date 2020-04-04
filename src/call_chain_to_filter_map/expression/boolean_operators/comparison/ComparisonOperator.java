package call_chain_to_filter_map.expression.boolean_operators.comparison;

import call_chain_to_filter_map.expression.boolean_operators.BooleanOperator;
import call_chain_to_filter_map.expression.polynomial.Polynomial;
import call_chain_to_filter_map.set.Bound;
import call_chain_to_filter_map.set.RealNumbersSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public abstract class ComparisonOperator extends BooleanOperator {
    /**
     * Constructs set of valid values for {@link Greater} or {@link Less}.
     *
     * @param type <var>false</var> for {@link Greater}, <var>true</var> for {@link Less}
     * @return the set of values satisfying {@link Greater} or {@link Less}
     */
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

    /**
     * @param x {@link Double} to check
     * @return <var>true</var> if and only if <var>x</var> is integer, <var>false</var> otherwise
     */
    protected static boolean isInteger(Double x) {
        return (!Double.isInfinite(x) && x == Math.floor(x));
    }

    /**
     * @param x {@link Double} to check
     * @return <var>true</var> if and only if <var>x</var> is integer or infinity, <var>false</var> otherwise
     */
    protected static boolean isInfinityOrInteger(Double x) {
        return Double.isInfinite(x) || isInteger(x);
    }

    /**
     * Constructs comparison operation.
     *
     * @param arg1 first argument
     * @param arg2 secon argument
     * @param operator symbolic representation
     */
    public ComparisonOperator(Polynomial arg1, Polynomial arg2, String operator) {
        super(arg1, arg2, operator);
    }
}
