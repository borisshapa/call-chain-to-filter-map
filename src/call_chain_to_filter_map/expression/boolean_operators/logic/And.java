package call_chain_to_filter_map.expression.boolean_operators.logic;

import call_chain_to_filter_map.expression.Expression;
import call_chain_to_filter_map.expression.boolean_operators.BooleanOperator;
import call_chain_to_filter_map.expression.polynomial.Polynomial;
import call_chain_to_filter_map.set.RealNumbersSet;

/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class And extends LogicOperator {

    /**
     * Constructs logic <var>and</var>.
     *
     * @param arg1 first argument
     * @param arg2 second argument
     */
    public And(BooleanOperator arg1, BooleanOperator arg2) {
        super(arg1, arg2, "&");
    }

    @Override
    public RealNumbersSet getValuesSet() {
        return getLogicValuesSet(RealNumbersSet::intersection);
    }

    @Override
    public Expression compose(Polynomial polynomial) {
        return new And((BooleanOperator) arg1.compose(polynomial), (BooleanOperator) arg2.compose(polynomial));
    }
}
