package set;

public class Bound implements Comparable<Bound> {
    public boolean inclusive;
    private Double value;

    public Bound(Double value, Boolean inclusive) {
        this.value = value;
        this.inclusive = inclusive;
    }

    public Bound(Double value) {
        this.value = value;
        this.inclusive = value != Double.POSITIVE_INFINITY && value != Double.NEGATIVE_INFINITY;
    }

    public Bound(Bound bound) {
        this.value = bound.value;
        this.inclusive = bound.inclusive;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public int compareTo(Bound bound) {
        return value.compareTo(bound.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
