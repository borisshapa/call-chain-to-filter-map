package call_chain_to_filter_map.expression.polynomial;

import call_chain_to_filter_map.expression.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class Polynomial implements Expression {
    /**
     * Name of variable in polynomial
     */
    public static final String VARIABLE_NAME = "element";

    private double[] coefficients;
    private int degree;

    /**
     * Constructs <var>coefficient</var> * <var>element</var> ^ degree.
     *
     * @param coefficient coefficient
     * @param degree      degree of <var>element</var>
     */
    public Polynomial(double coefficient, int degree) {
        if (degree < 0) {
            throw new IllegalArgumentException("An integer non-negative degree of operations.polynomial was expected");
        }
        this.coefficients = new double[degree + 1];
        this.coefficients[degree] = coefficient;
        this.degree = reduceDegree();
    }

    /**
     * Constructs polynomial by coefficients passed.
     *
     * @param coefficients polynomial coefficients
     */
    public Polynomial(double[] coefficients) {
        this.coefficients = Arrays.copyOf(coefficients, coefficients.length + 1);
        this.degree = reduceDegree();
    }

    /**
     * @return polynomial degree
     */
    public int reduceDegree() {
        int result = 0;
        for (int i = coefficients.length - 1; i >= 0; i--) {
            if (coefficients[i] != 0) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * @return polynomial degree
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Returns the sign of the polynomial as the element tends to infinity.
     *
     * @return -1 if limit is negative, 1 if limit is positive
     */
    public int signAtNegativeInfinity() {
        return (degree % 2 == 0 && coefficients[degree] > 0) || (degree % 2 != 0 && coefficients[degree] < 0) ? 1 : -1;
    }

    /**
     * Adds this polynomial with the passed.
     *
     * @param polynomial what to add
     * @return sum of two polynomials
     */
    public Polynomial add(Polynomial polynomial) {
        Polynomial result = new Polynomial(0, Math.max(this.degree, polynomial.degree));
        for (int i = 0; i <= this.degree; i++) {
            result.coefficients[i] += this.coefficients[i];
        }
        for (int i = 0; i <= polynomial.degree; i++) {
            result.coefficients[i] += polynomial.coefficients[i];
        }
        result.degree = result.reduceDegree();
        return result;
    }

    /**
     * Subtracts this polynomial with the passed.
     *
     * @param polynomial what to subtract
     * @return difference of two polynomials
     */
    public Polynomial subtract(Polynomial polynomial) {
        Polynomial result = new Polynomial(0, Math.max(this.degree, polynomial.degree));
        for (int i = 0; i <= this.degree; i++) {
            result.coefficients[i] += this.coefficients[i];
        }
        for (int i = 0; i <= polynomial.degree; i++) {
            result.coefficients[i] -= polynomial.coefficients[i];
        }
        result.degree = result.reduceDegree();
        return result;
    }

    /**
     * Multiplies this polynomial by the transmitted one.
     *
     * @param polynomial what to multiply
     * @return result of multiplication
     */
    public Polynomial multiply(Polynomial polynomial) {
        Polynomial result = new Polynomial(0, this.degree + polynomial.degree);
        for (int i = 0; i <= this.degree; i++) {
            for (int j = 0; j <= polynomial.degree; j++) {
                result.coefficients[i + j] += this.coefficients[i] * polynomial.coefficients[j];
            }
        }
        result.degree = result.reduceDegree();
        return result;
    }

    /**
     * @return the coefficient before the element in the degree passed.
     */
    public double getCoefficient(int degree) {
        if (degree > this.degree) {
            return 0;
        }
        return coefficients[degree];
    }

    /**
     * @return polynomial derivative
     */
    public Polynomial differentiate() {
        if (degree == 0) {
            return new Polynomial(0, 0);
        }
        Polynomial result = new Polynomial(0, degree - 1);
        result.degree = degree - 1;
        for (int i = 0; i < degree; i++) {
            result.coefficients[i] = (i + 1) * coefficients[i + 1];
        }
        return result;
    }

    private double compose(int level, double value) {
        double result = 1;
        for (int i = level - 1; i >= 0; i--) {
            result = result * value + coefficients[i];
        }
        return result;
    }

    private int getSign(Polynomial polynomial, int level, double bound, List<List<Double>> roots) {
        double value = polynomial.compose(level, bound);
        if (value == 0) {
            roots.get(level).add(bound);
            return 0;
        }
        return (value > 0) ? 1 : -1;
    }

    private double binarySearch(double left, double right, Predicate<Double> predicate) {
        double mid = left;
        for (int i = 0; i < 65; i++) {
            mid = (left + right) / 2;
            if (predicate.test(mid)) {
                left = mid;
            } else {
                right = mid;
            }
        }
        return mid;
    }

    /**
     * @return polynomial roots
     */
    public List<Double> getRoots() {
        if (degree == 0) {
            return List.of();
        }

        Polynomial[] derivatives = new Polynomial[degree + 1];
        List<List<Double>> roots = new ArrayList<>(degree + 1);

        for (int i = 0; i <= degree; i++) {
            roots.add(new ArrayList<>());
        }

        Polynomial normPoly = new Polynomial(coefficients);
        for (int i = 0; i < normPoly.coefficients.length - 1; i++) {
            normPoly.coefficients[i] /= normPoly.coefficients[normPoly.degree];
        }
        derivatives[degree] = normPoly.subtract(new Polynomial(1, degree));
        for (int i = degree - 1; i >= 0; i--) {
            derivatives[i] = derivatives[i + 1].differentiate();
            for (int j = 0; j <= derivatives[i].degree; j++) {
                derivatives[i].coefficients[j] /= i + 1;
            }
        }

        roots.get(1).add(-derivatives[1].getCoefficient(0));
        for (int i = 2; i <= degree; i++) {
            double bound = 0;
            for (int j = 0; j < i; j++) {
                bound = Math.max(bound, Math.abs(derivatives[i].coefficients[j]));
            }
            bound += 1.0;

            for (int j = 0; j <= roots.get(i - 1).size(); j++) {
                double boundL = (j == 0) ? -bound : roots.get(i - 1).get(j - 1);
                int signL = getSign(derivatives[i], i, boundL, roots);
                if (signL == 0) {
                    continue;
                }

                double boundR = (j == roots.get(i - 1).size()) ? bound : roots.get(i - 1).get(j);
                int signR = getSign(derivatives[i], i, boundR, roots);
                if (signR == 0 || signL == signR) {
                    continue;
                }

                double boundNeg = (signL < 0) ? boundL : boundR;
                double boundPos = (signL < 0) ? boundR : boundL;

                final int ind = i;
                roots.get(i).add(binarySearch(boundNeg, boundPos, (x) -> derivatives[ind].compose(ind, x) < 0));
            }
        }

        return new ArrayList<>(roots.get(degree));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Polynomial)) {
            return false;
        }

        Polynomial polynomial = (Polynomial) obj;
        if (this.degree != polynomial.degree) {
            return false;
        }
        for (int i = 0; i <= this.degree; i++) {
            if (this.coefficients[i] != polynomial.coefficients[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Polynomial compose(Polynomial polynomial) {
        Polynomial result = new Polynomial(0, 0);
        for (int i = this.degree; i >= 0; i--) {
            result = new Polynomial(this.coefficients[i], 0).add(result.multiply(polynomial));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int openingBrackets = degree;

        for (int i = degree; i >= 0; i--) {
            if (coefficients[i] == 0) {
                openingBrackets--;
                continue;
            }
            if (coefficients[i] < 0) {
                sb.append("-");
            } else if (i < degree) {
                sb.append("+");
            }

            int coefficientAbs = Math.abs(((Double) coefficients[i]).intValue());
            if (coefficientAbs == 1) {
                if (i == 0) {
                    sb.append(coefficientAbs);
                } else {
                    sb.append("(".repeat(i - 1));
                    sb.append(VARIABLE_NAME);
                    sb.append(String.format("*%s)", VARIABLE_NAME).repeat(i - 1));
                }
            } else {
                sb.append("(".repeat(i));
                sb.append(coefficientAbs);
                sb.append(String.format("*%s)", VARIABLE_NAME).repeat(i));
            }
            if (i < degree) {
                sb.append(")");
            }
        }
        if (sb.toString().isEmpty()) {
            return "0";
        }
        return "(".repeat(openingBrackets) + sb.toString();
    }
}
