package expression;

import expression.polynomial.Polynomial;

public interface Expression {
    Expression compose(Polynomial polynomial);
}
