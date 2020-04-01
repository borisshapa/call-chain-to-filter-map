package operators.logic;

import operators.BooleanOperator;
import polynomial.Polynomial;

public class And extends AbstractLogicOperator {
    public And(BooleanOperator arg1, BooleanOperator arg2) {
        super(arg1, arg2);
    }

    public String toString() {
        return toString("&");
    }
}
