package set.bound;

public class Bound implements Comparable<Bound> {
    public boolean inclusive;

    public Bound(Bound bound) {
        this.inclusive = bound.inclusive;
    }

    public Bound(Boolean inclusive) {
        this.inclusive = inclusive;
    }

    @Override
    public int compareTo(Bound bound) {
        if (this instanceof Infinity && bound instanceof Infinity) {
            return 0;
        }
        if (this instanceof NegativeInfinity && bound instanceof NegativeInfinity) {
            return 0;
        }

        if (this instanceof Infinity || bound instanceof NegativeInfinity) {
            return 1;
        }
        if (this instanceof NegativeInfinity || bound instanceof Infinity) {
            return -1;
        }
        Integer point1 = ((Point) this).number;
        Integer point2 = ((Point) bound).number;
        return point1.compareTo(point2);
    }
}
