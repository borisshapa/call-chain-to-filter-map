package expression.polynomial;

import expression.Expression;

public class Polynomial implements Expression {
    static final String VARIABLE_NAME = "element";

    private int[] coefficients;
    private int degree;

    public Polynomial(int coefficient, int degree) {
        if (degree < 0) {
            throw new IllegalArgumentException("An integer non-negative degree of operations.polynomial was expected");
        }
        this.coefficients = new int[degree + 1];
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

    public Polynomial getConstantTerm() {
        return new Polynomial(coefficients[0], 0);
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
            if (i != degree) {
                if (coefficients[i] >= 0) {
                    sb.append("+");
                } else {
                    sb.append("-");
                }
            }
            int coefficientAbs = Math.abs(coefficients[i]);
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
            if (openingBrackets < degree) {
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
