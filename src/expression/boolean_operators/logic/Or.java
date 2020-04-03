package expression.boolean_operators.logic;

import expression.Expression;
import expression.boolean_operators.BooleanOperator;
import expression.polynomial.Polynomial;
import set.RealNumbersSet;

public class Or extends LogicOperator {
    public Or(BooleanOperator arg1, BooleanOperator arg2) {
        super(arg1, arg2, "|");
    }

    @Override
    public RealNumbersSet getValuesSet() {
        return getLogicValuesSet(RealNumbersSet::union);
    }

    @Override
    public Expression compose(Polynomial polynomial) {
        return new And((BooleanOperator) arg1.compose(polynomial), (BooleanOperator) arg2.compose(polynomial));
    }
}
