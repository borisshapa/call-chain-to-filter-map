package expression.boolean_operators;

import expression.Expression;
import expression.boolean_operators.logic.LogicOperator;
import expression.polynomial.Polynomial;
import set.RealNumbersSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BooleanOperator implements Expression {
    protected Expression arg1;
    protected Expression arg2;
    private String operator;

    public BooleanOperator(Expression arg1, Expression arg2, String operator) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.operator = operator;
    }

    public abstract RealNumbersSet getValuesSet();

    @Override
    public String toString() {
        return arg1.toString() + operator + arg2.toString();
    }
}
