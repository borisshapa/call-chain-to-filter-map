package operators.comparison;

import operators.BooleanOperator;
import polynomial.Polynomial;

public class AbstractComparisonOperator implements BooleanOperator {
    private Polynomial arg1, arg2;

    public AbstractComparisonOperator(Polynomial arg1, Polynomial arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String toString(String operand) {
        return "(" + arg1.toString() + operand + arg2.toString() + ")";
    }
}
