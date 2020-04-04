package call_chain_to_filter_map.expression.boolean_operators.comparison;

import call_chain_to_filter_map.expression.Expression;
import call_chain_to_filter_map.expression.polynomial.Polynomial;
import call_chain_to_filter_map.set.RealNumbersSet;

/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class Greater extends ComparisonOperator {
    @Override
    public RealNumbersSet getValuesSet() {
        return getInequalityValuesSet(false);
    }

    /**
     * Constructs ">" operation.
     *
     * @param arg1 first argument
     * @param arg2 second argument
     */
    public Greater(Polynomial arg1, Polynomial arg2) {
        super(arg1, arg2, ">");
    }

    @Override
    public Expression compose(Polynomial polynomial) {
        return new Greater(((Polynomial) arg1).compose(polynomial), ((Polynomial) arg2).compose(polynomial));
    }
}
