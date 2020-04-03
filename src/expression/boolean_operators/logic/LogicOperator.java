package expression.boolean_operators.logic;

import expression.Expression;
import expression.boolean_operators.BooleanOperator;
import expression.polynomial.Polynomial;
import set.RealNumbersSet;

import java.util.Map;
import java.util.function.BinaryOperator;

public abstract class LogicOperator extends BooleanOperator {

    protected RealNumbersSet getLogicValuesSet(BinaryOperator<RealNumbersSet> setsFun) {
        RealNumbersSet set1 = ((BooleanOperator) arg1).getValuesSet();
        RealNumbersSet set2 = ((BooleanOperator) arg2).getValuesSet();
        return setsFun.apply(set1, set2);
    }

    public LogicOperator(Expression arg1, Expression arg2, String operator) {
        super(arg1, arg2, operator);
    }
}
