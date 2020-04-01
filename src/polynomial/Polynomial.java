package polynomial;

public class Polynomial {
    static final String VARIABLE_NAME = "element";

    private int[] coefficients;
    private int degree;

    public Polynomial(int coefficient, int degree) {
        if (degree < 0) {
            throw new IllegalArgumentException("An integer non-negative degree of polynomial was expected");
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
        for (int i = degree; i >= 0; i--) {
            if (coefficients[i] == 0) {
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
                    sb.append(VARIABLE_NAME);
                    sb.append(("*" + VARIABLE_NAME).repeat(i - 1));
                }
            } else {
                sb.append(coefficientAbs);
                sb.append(("*" + VARIABLE_NAME).repeat(i));
            }
        }
        if (sb.toString().isEmpty()) {
            sb.append(0);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Polynomial poly = new Polynomial(2, 2);
        Polynomial poly2 = new Polynomial(-5, 1);
        Polynomial poly3 = new Polynomial(12, 1);
        System.out.println(poly.add(poly2).compose(poly3).toString());
    }
}
