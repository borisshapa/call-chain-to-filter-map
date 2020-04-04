package call_chain_to_filter_map.set;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class Segment {
    private Bound lowerBound;
    private Bound upperBound;

    /**
     * Constructs a segment with boundaries passed.
     *
     * @param lowerBound lower boundary of segment
     * @param upperBound upper boundary of segment
     * @see Bound
     */
    public Segment(Bound lowerBound, Bound upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

        if (lowerBound.compareTo(upperBound) > 0) {
            throw new IllegalArgumentException("The lower boundary of the segment cannot be to the right of the upper");
        }
    }

    /**
     * Constructs a segment based on {@link Double} values of boundaries.
     * Boundary is included if it is number and not included if it is infinity.
     *
     * @param lowerBound value of lower boundary
     * @param upperBound value of upper boundary
     */
    public Segment(Double lowerBound, Double upperBound) {
        this(new Bound(lowerBound), new Bound(upperBound));
    }

    /**
     * Constructs a segment based on {@link Double} value of lower boundary and upper {@link Bound}
     * @param lowerBound {@link Double} value of lower boundary
     * @param upperBound upper boundary
     */
    public Segment(Double lowerBound, Bound upperBound) {
        this(new Bound(lowerBound), upperBound);
    }

    /**
     * Constructs a segment based on lower {@link Bound} and {@link Double} value of upper boundary.
     * @param lowerBound lower boundary
     * @param upperBound {@link Double} value of upper boundary
     */
    public Segment(Bound lowerBound, Double upperBound) {
        this(lowerBound, new Bound(upperBound));
    }

    /**
     * Construct [<var>point</var> .. <var>point</var>] if <var>point</var> is number and
     * (<var>point</var> .. <var>point</var>) if <var>point</var> is infinity
     *
     * @param point {@link Double} value of point
     */
    public Segment(Double point) {
        this(new Bound(point), new Bound(point));
    }

    /**
     * @return lower boundary of segment
     */
    public Bound getLowerBound() {
        return lowerBound;
    }

    /**
     * @return upper boundary of segment
     */
    public Bound getUpperBound() {
        return upperBound;
    }

    private Segment docking(Bound bound1, Bound bound2, BinaryOperator<Boolean> predicate, Supplier<Segment> returnSegment) {
        return (predicate.apply(bound1.inclusive, bound2.inclusive))
                ? returnSegment.get()
                : null;
    }

    private void inclusion(Segment segment1, Segment segment2, Segment result,
                           BinaryOperator<Boolean> inclusionFactor) {
        Bound lb1 = segment1.lowerBound;
        Bound lb2 = segment2.lowerBound;

        Bound ub1 = segment1.upperBound;
        Bound ub2 = segment2.upperBound;

        if (lb1.compareTo(lb2) == 0) {
            result.lowerBound.inclusive = inclusionFactor.apply(lb1.inclusive, lb2.inclusive);
        }
        if (ub1.compareTo(ub2) == 0) {
            result.upperBound.inclusive = inclusionFactor.apply(ub1.inclusive, ub2.inclusive);
        }
    }

    /**
     * Combines this segment with the passed.
     *
     * @param segment with what to combine
     * @return <var>null</var> if the union of the segments is not one segment and the union of two otherwise.
     */
    public Segment union(Segment segment) {
        BinaryOperator<Boolean> inclusionFactor = (a, b) -> (a || b);

        Bound segmentLB = segment.lowerBound;
        Bound segmentUB = segment.upperBound;

        if (lowerBound.compareTo(segmentUB) > 0 || upperBound.compareTo(segmentLB) < 0) {
            return null;
        }

        Bound resultLB = new Bound(lowerBound);
        Bound resultUB = new Bound(upperBound);

        if (lowerBound.compareTo(segmentUB) == 0) {
            return docking(lowerBound, segmentUB, inclusionFactor, () -> new Segment(segmentLB, upperBound));
        }
        if (upperBound.compareTo(segmentLB) == 0) {
            return docking(upperBound, segmentLB, inclusionFactor, () -> new Segment(lowerBound, segmentUB));
        }

        inclusion(this, segment, new Segment(resultLB, resultUB), inclusionFactor);
        if (lowerBound.compareTo(segmentLB) > 0) {
            resultLB = segmentLB;
        }
        if (upperBound.compareTo(segmentUB) < 0) {
            resultUB = segmentUB;
        }
        return new Segment(resultLB, resultUB);
    }

    /**
     * Crosses two segments.
     *
     * @param segment what to cross with
     * @return <var>null</var> if the intersection is an empty set and the intersection of two otherwise.
     */
    public Segment intersection(Segment segment) {
        BinaryOperator<Boolean> inclusionFactor = (a, b) -> (a && b);

        Bound segmentLB = segment.lowerBound;
        Bound segmentUB = segment.upperBound;

        if (lowerBound.compareTo(segmentUB) > 0 || upperBound.compareTo(segmentLB) < 0) {
            return null;
        }

        Bound resultLB = new Bound(lowerBound);
        Bound resultUB = new Bound(upperBound);
        if (lowerBound.compareTo(segmentUB) == 0) {
            return docking(lowerBound, segmentUB, inclusionFactor, () -> new Segment(lowerBound, segmentUB));
        }

        if (upperBound.compareTo(segmentLB) == 0) {
            return docking(upperBound, segmentLB, inclusionFactor, () -> new Segment(segmentLB, upperBound));
        }

        inclusion(this, segment, new Segment(resultLB, resultUB), inclusionFactor);
        if (lowerBound.compareTo(segmentLB) < 0) {
            resultLB = segmentLB;
        }
        if (upperBound.compareTo(segmentUB) > 0) {
            resultUB = segmentUB;
        }

        if (resultLB.compareTo(resultUB) == 0 && !(resultLB.inclusive && resultUB.inclusive)) {
            return null;
        }
        return new Segment(resultLB, resultUB);
    }

    @Override
    public String toString() {
        return ((lowerBound.inclusive) ? "[" : "(") +
                lowerBound.toString() + " .. " + upperBound.toString() +
                ((upperBound.inclusive) ? "]" : ")");
    }
}
