package operators.logic;

import operators.BooleanOperator;

public class AbstractLogicOperator implements BooleanOperator {
    private BooleanOperator arg1, arg2;

    public AbstractLogicOperator(BooleanOperator arg1, BooleanOperator arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}
