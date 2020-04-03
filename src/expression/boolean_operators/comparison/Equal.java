package expression.boolean_operators.comparison;

import expression.Expression;
import expression.boolean_operators.BooleanOperator;
import expression.polynomial.Polynomial;
import set.RealNumbersSet;
import set.Segment;

import java.util.List;
import java.util.stream.Collectors;

public class Equal extends ComparisonOperator {
    @Override
    public RealNumbersSet getValuesSet() {
        Polynomial diff = ((Polynomial) arg1).subtract((Polynomial) arg2);
        List<Double> roots = diff.getRoots();
        return new RealNumbersSet(roots
                .stream()
                .filter(ComparisonOperator::isInteger)
                .map(Segment::new).collect(Collectors.toList()));

    }

    public Equal(Polynomial arg1, Polynomial arg2) {
        super(arg1, arg2, "=");
    }

    @Override
    public Expression compose(Polynomial polynomial) {
        return new Equal(((Polynomial) arg1).compose(polynomial), ((Polynomial) arg2).compose(polynomial));
    }
}
