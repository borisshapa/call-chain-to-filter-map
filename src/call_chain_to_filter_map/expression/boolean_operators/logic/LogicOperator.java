package call_chain_to_filter_map.expression.boolean_operators.logic;

import call_chain_to_filter_map.expression.Expression;
import call_chain_to_filter_map.expression.boolean_operators.BooleanOperator;
import call_chain_to_filter_map.set.RealNumbersSet;

import java.util.function.BinaryOperator;

/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public abstract class LogicOperator extends BooleanOperator {

    /**
     * Collects a set of values from sets of arguments according to an operation.
     *
     * @param setsFun action on sets of arguments.
     * @return the set of values satisfying a logical operation.
     */
    protected RealNumbersSet getLogicValuesSet(BinaryOperator<RealNumbersSet> setsFun) {
        RealNumbersSet set1 = ((BooleanOperator) arg1).getValuesSet();
        RealNumbersSet set2 = ((BooleanOperator) arg2).getValuesSet();
        return setsFun.apply(set1, set2);
    }

    /**
     * Constructs logic operation.
     *
     * @param arg1 first argument
     * @param arg2 second argument
     * @param operator symbolic representation
     */
    public LogicOperator(Expression arg1, Expression arg2, String operator) {
        super(arg1, arg2, operator);
    }
}
