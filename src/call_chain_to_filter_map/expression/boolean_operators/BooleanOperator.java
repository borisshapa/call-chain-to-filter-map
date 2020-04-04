package call_chain_to_filter_map.expression.boolean_operators;

import call_chain_to_filter_map.expression.Expression;
import call_chain_to_filter_map.set.RealNumbersSet;

/**
 * Operation returning a {@link java.lang.Boolean} value.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public abstract class BooleanOperator implements Expression {
    /**
     * first operand
     */
    protected Expression arg1;
    /**
     * second operand
     */
    protected Expression arg2;
    private String operator;

    /**
     * Construct operation with The operation with the arguments passed and the symbolic representation.
     *
     * @param arg1 first operand
     * @param arg2 second operand
     * @param operator symbolic representation
     */
    public BooleanOperator(Expression arg1, Expression arg2, String operator) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.operator = operator;
    }

    /**
     * @return the set of values satisfying the operation
     */
    public abstract RealNumbersSet getValuesSet();

    @Override
    public String toString() {
        return arg1.toString() + operator + arg2.toString();
    }
}
