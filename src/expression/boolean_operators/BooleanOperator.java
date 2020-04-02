package expression.boolean_operators;

import expression.Expression;
import expression.boolean_operators.comparison.Equal;
import expression.polynomial.Polynomial;

public class BooleanOperator implements Expression {
    private Expression arg1, arg2;
    String operator;

    public BooleanOperator(Expression arg1, Expression arg2, String operator) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "(" + arg1.toString() + operator + arg2.toString() + ")";
    }

    @Override
    public Expression compose(Polynomial polynomial) {
        return new BooleanOperator(arg1.compose(polynomial), arg2.compose(polynomial), operator);
    }
}
