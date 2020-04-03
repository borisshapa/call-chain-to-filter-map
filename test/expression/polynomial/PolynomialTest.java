package expression.polynomial;

import org.junit.Assert;
import org.junit.Test;
import set.RealNumbersSet;

import java.util.List;

public class PolynomialTest {
    private void valid(final List<Double> roots, final List<Double> expected) {
        Assert.assertEquals(expected.size(), roots.size());
        for (int i = 0; i < roots.size(); i++) {
            Assert.assertTrue(Math.abs(roots.get(i) - expected.get(i)) <= 0.0001);
        }
    }

    @Test
    public void degreeOne() {
        Polynomial polynomial = new Polynomial(1, 1).add(new Polynomial(5, 0));
        valid(polynomial.getRoots(), List.of(-5.0));
    }

    @Test
    public void degreeTwo() {
        Polynomial polynomial = new Polynomial(1, 2).add(new Polynomial(2, 1)).add(new Polynomial(1, 0));
        valid(polynomial.getRoots(), List.of(-1.0, -1.0));

        Polynomial polynomial1 = new Polynomial(2, 2).add(new Polynomial(-10, 1)).add(new Polynomial(12, 0));
        valid(polynomial1.getRoots(), List.of(2.0, 3.0));

        Polynomial polynomial2 = new Polynomial(1, 2).add(new Polynomial(1, 1)).add(new Polynomial(1, 0));
        valid(polynomial2.getRoots(), List.of());
    }

    @Test
    public void degreeThree() {
        Polynomial polynomial = new Polynomial(1, 3).add(new Polynomial(3, 2)).add(new Polynomial(-6, 1)).add(new Polynomial(-18, 0));
        valid(polynomial.getRoots(), List.of(-3.0, -2.449489, 2.449489));

        Polynomial polynomial1 = new Polynomial(1, 3).add(new Polynomial(1, 2)).add(new Polynomial(1, 1)).add(new Polynomial(1, 0));
        valid(polynomial1.getRoots(), List.of(-1.0));
    }

    @Test
    public void degreeSix() {
        Polynomial polynomial = new Polynomial(2, 6).add(new Polynomial(-6, 5)).add(new Polynomial(3, 4)).add(new Polynomial(-6, 3)).add(new Polynomial(9, 2)).add(new Polynomial(-4, 1)).add(new Polynomial(2, 0));
        valid(polynomial.getRoots(), List.of(1.0, 2.65255));
    }
}
