package call_chain_to_filter_map.set;

/**
 * Segment boundary.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class Bound implements Comparable<Bound> {
    /**
     * is the boundary included in the set.
     */
    public boolean inclusive;
    private Double value;

    /**
     * Constructs a bound.
     *
     * @param value {@link Double} value of boundary
     * @param inclusive whether to include a point in the set
     */
    public Bound(Double value, Boolean inclusive) {
        this.value = value;
        this.inclusive = inclusive;
    }

    /**
     * Constructs a bound, included if it is number and not included if it is infinity.
     *
     * @param value {@link Double} value of boundary
     */
    public Bound(Double value) {
        this.value = value;
        this.inclusive = !Double.isInfinite(value);
    }

    /**
     * Copy constructor.
     *
     * @param bound copy of what to construct
     */
    public Bound(Bound bound) {
        this.value = bound.value;
        this.inclusive = bound.inclusive;
    }

    /**
     * @return {@link Double} value of boundary.
     */
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
