package call_chain_to_filter_map.expression;

import call_chain_to_filter_map.expression.polynomial.Polynomial;

/**
 * Argument of operation on array elements
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public interface Expression {
    /**
     * Substitute a polynomial into the expression instead of an "element".
     *
     * @param polynomial polynomial for substitution
     * @return expression with substitution
     */
    Expression compose(Polynomial polynomial);
}
