package expression.boolean_operators.logic;

import expression.boolean_operators.BooleanOperator;

public class And extends BooleanOperator {
    public And(BooleanOperator arg1, BooleanOperator arg2) {
        super(arg1, arg2, "&");
    }
}
