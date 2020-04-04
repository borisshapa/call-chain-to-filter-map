package call_chain_to_filter_map.expression.boolean_operators.comparison;

import call_chain_to_filter_map.expression.Expression;
import call_chain_to_filter_map.expression.polynomial.Polynomial;
import call_chain_to_filter_map.set.RealNumbersSet;
import call_chain_to_filter_map.set.Segment;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class Equal extends ComparisonOperator {
    @Override
    public RealNumbersSet getValuesSet() {
        Polynomial diff = ((Polynomial) arg1).subtract((Polynomial) arg2);
        if (diff.getDegree() == 0 && diff.getCoefficient(0) == 0.0) {
            return RealNumbersSet.ALL;
        }
        List<Double> roots = diff.getRoots();
        return new RealNumbersSet(roots
                .stream()
                .filter(ComparisonOperator::isInteger)
                .map(Segment::new).collect(Collectors.toList()));

    }

    /**
     * Constructs "=" operation.
     *
     * @param arg1 first argument
     * @param arg2 second argument
     */
    public Equal(Polynomial arg1, Polynomial arg2) {
        super(arg1, arg2, "=");
    }

    @Override
    public Expression compose(Polynomial polynomial) {
        return new Equal(((Polynomial) arg1).compose(polynomial), ((Polynomial) arg2).compose(polynomial));
    }
}
