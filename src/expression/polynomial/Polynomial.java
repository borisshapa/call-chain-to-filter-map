package expression.polynomial;

import expression.Expression;

import java.util.ArrayList;
import java.util.List;

public class Polynomial implements Expression {
    public static final String VARIABLE_NAME = "element";

    private double[] coefficients;
    private int degree;

    public Polynomial(double coefficient, int degree) {
        if (degree < 0) {
            throw new IllegalArgumentException("An integer non-negative degree of operations.polynomial was expected");
        }
        this.coefficients = new double[degree + 1];
        this.coefficients[degree] = coefficient;
        this.degree = getDegree();
    }

    public int getDegree() {
        int result = 0;
        for (int i = coefficients.length - 1; i >= 0; i--) {
            if (coefficients[i] != 0) {
                result = i;
                break;
            }
        }
        return result;
    }

    public int signAtNegativeInfinity() {
        return (degree % 2 == 0 && coefficients[degree] > 0) || (degree % 2 != 0 && coefficients[degree] < 0) ? 1 : -1;
    }

    public Polynomial add(Polynomial polynomial) {
        Polynomial result = new Polynomial(0, Math.max(this.degree, polynomial.degree));
        for (int i = 0; i <= this.degree; i++) {
            result.coefficients[i] += this.coefficients[i];
        }
        for (int i = 0; i <= polynomial.degree; i++) {
            result.coefficients[i] += polynomial.coefficients[i];
        }
        result.degree = result.getDegree();
        return result;
    }

    public Polynomial subtract(Polynomial polynomial) {
        Polynomial result = new Polynomial(0, Math.max(this.degree, polynomial.degree));
        for (int i = 0; i <= this.degree; i++) {
            result.coefficients[i] += this.coefficients[i];
        }
        for (int i = 0; i <= polynomial.degree; i++) {
            result.coefficients[i] -= polynomial.coefficients[i];
        }
        result.degree = result.getDegree();
        return result;
    }

    public Polynomial multiply(Polynomial polynomial) {
        Polynomial result = new Polynomial(0, this.degree + polynomial.degree);
        for (int i = 0; i <= this.degree; i++) {
            for (int j = 0; j <= polynomial.degree; j++) {
                result.coefficients[i + j] += this.coefficients[i] * polynomial.coefficients[j];
            }
        }
        result.degree = result.getDegree();
        return result;
    }

    public Double getConstantTerm() {
        return coefficients[0];
    }

    private double polynom(int n, double x, double[] k) {
        double s = 1;
        for (int i = n - 1; i >= 0; i--) {
            s = s * x + k[i];
        }
        return s;
    }

    private double rootInSegment(int degree, double boundNeg, double boundPos, double[] coefficients) {
        while (true) {
            double x = 0.5 * (boundNeg + boundPos);
            if (x == boundNeg || x == boundPos) {
                return x;
            }
            if (polynom(degree, x, coefficients) < 0) {
                boundNeg = x;
            } else {
                boundPos = x;
            }
        }
    }

    private void stepUp(int level, double[][] a, double[][] b, int[] rootsCount) {
        double major = 0;
        for (int i = 0; i < level; i++) {
            double s = Math.abs(a[level][i]);
            if (s > major) {
                major = s;
            }
        }
        major += 1.0;

        rootsCount[level] = 0;
        for (int i = 0; i <= rootsCount[level - 1]; i++) {
            int signL, signR;
            double boundL, boundR;
            double boundNeg, boundPos;

            if (i == 0) {
                boundL = -major;
            } else {
                boundL = b[level - 1][i - 1];
            }

            double rb = polynom(level, boundL, a[level]);

            if (rb == 0) {
                b[level][rootsCount[level]] = boundL;
                rootsCount[level]++;
                continue;
            }

            if (rb > 0) {
                signL = 1;
            } else {
                signL = -1;
            }

            if (i == rootsCount[level - 1]) {
                boundR = major;
            } else {
                boundR = b[level - 1][i];
            }

            rb = polynom(level, boundR, a[level]);

            if (rb == 0) {
                b[level][rootsCount[level]] = boundR;
                rootsCount[level]++;
                continue;
            }

            if (rb > 0) {
                signR = 1;
            } else {
                signR = -1;
            }

            if (signL == signR) {
                continue;
            }

            if (signL < 0) {
                boundNeg = boundL;
                boundPos = boundR;
            } else {
                boundNeg = boundR;
                boundPos = boundL;
            }

            b[level][rootsCount[level]] = rootInSegment(level, boundNeg, boundPos, a[level]);
            rootsCount[level]++;
        }
    }

    public List<Double> getRoots() {
        double[][] a = new double[degree + 1][];
        double[][] b = new double[degree + 1][];
        int[] rootsCount = new int[degree + 1];

        for (int i = 0; i <= degree; i++) {
            a[i] = new double[i];
            b[i] = new double[i];
        }

        for (int i = 0; i < degree; i++) {
            a[degree][i] = coefficients[i] / coefficients[degree];
        }

        for (int i1 = degree, i = degree - 1; i > 0; i1 = i, i--) {
            for (int j1 = i, j = i - 1; j >= 0; j1 = j, j--) {
                a[i][j] = a[i1][j1] * j1 / i1;
            }
        }

        rootsCount[1] = 1;
        b[1][0] = -a[1][0];
        for (int i = 2; i <= degree; i++) {
            stepUp(i, a, b, rootsCount);
        }

        List<Double> result = new ArrayList<>();
        for (int i = 0; i < rootsCount[degree]; i++) {
            result.add(b[degree][i]);
        }
        return result;
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
            if (i < degree) {
                if (coefficients[i] >= 0) {
                    sb.append("+");
                } else {
                    sb.append("-");
                }
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
            sb.append(0);
        }
        return "(".repeat(openingBrackets) + sb.toString();
    }

    public static void main(String[] args) {
        Polynomial poly = new Polynomial(2, 2);
        Polynomial poly2 = new Polynomial(-5, 1);
        Polynomial poly3 = new Polynomial(12, 1);
        System.out.println(poly.add(poly2).compose(poly3).toString());
    }
}
