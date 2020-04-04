package call_chain_to_filter_map.set;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class RealNumbersSet {
    /**
     * A set containing all real numbers.
     */
    public final static RealNumbersSet ALL = new RealNumbersSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    private final static Segment INF = new Segment(Double.POSITIVE_INFINITY);

    private List<Segment> segments;

    /**
     * Constructs a set which is a segment.
     *
     * @param segment set
     */
    public RealNumbersSet(Segment segment) {
        this();
        add(segment);
    }

    /**
     * Constructs a set which is a point ot infinity.
     *
     * @param point {@link Double} value of point.
     */
    public RealNumbersSet(Double point) {
        this(new Segment(point, point));
    }

    /**
     * Constructs a set which is a segment with boundaries passed.
     *
     * @param lowerBound {@link Double} value of lower boundary
     * @param upperBound {@link Double} value of upper boundary
     */
    public RealNumbersSet(Double lowerBound, Double upperBound) {
        this(new Bound(lowerBound), new Bound(upperBound));
    }

    /**
     * Constructs an empty set.
     */
    public RealNumbersSet() {
        segments = new ArrayList<>();
    }

    /**
     * Constructs a set which is segment with boundaries passed.
     *
     * @param lowerBound lower boundary of segment
     * @param upperBound upper boundary of segment
     */
    public RealNumbersSet(Bound lowerBound, Bound upperBound) {
        this(new Segment(lowerBound, upperBound));
    }

    /**
     * Constructs a new set by {@link List} of {@link Segment}s of values.
     *
     * @param segments the set of values specified by the {@link List} of {@link Segment}s
     */
    public RealNumbersSet(List<Segment> segments) {
        RealNumbersSet tmpSet = new RealNumbersSet();
        for (Segment segment : segments) {
            tmpSet = tmpSet.union(new RealNumbersSet(segment));
        }
        this.segments = tmpSet.segments;
    }

    /**
     * @return a set as {@link List} of disjoint {@link Segment}s sorted in ascending order.
     */
    public List<Segment> getSegments() {
        return segments;
    }

    private void add(Segment segment) {
        if (segment.getLowerBound().compareTo(segment.getUpperBound()) < 0
                || (segment.getLowerBound().compareTo(segment.getUpperBound()) == 0
                && segment.getLowerBound().inclusive && segment.getUpperBound().inclusive)) {
            segments.add(segment);
        }
    }

    private Segment segmentJoin(Segment accumulatedSegment, Segment addedSegment, RealNumbersSet generatedSet) {
        if (addedSegment == null) {
            return accumulatedSegment;
        }
        if (accumulatedSegment == null) {
            return addedSegment;
        }
        Segment union = accumulatedSegment.union(addedSegment);
        if (union == null) {
            generatedSet.add(accumulatedSegment);
            return addedSegment;
        }
        return union;
    }

    /**
     * Combines this set with the passed.
     *
     * @param set what to combine with
     * @return union of sets
     */
    public RealNumbersSet union(RealNumbersSet set) {
        int thisPtr = 0;
        int setPtr = 0;
        RealNumbersSet result = new RealNumbersSet();
        Segment currentSegment = null;
        while (thisPtr < segments.size() || setPtr < set.segments.size()) {
            Segment thisSegment = thisPtr >= segments.size() ? INF : segments.get(thisPtr);
            Segment segment = setPtr >= set.segments.size() ? INF : set.segments.get(setPtr);

            Bound thisLB = thisSegment.getLowerBound();
            Bound segmentLB = segment.getLowerBound();

            if (thisLB.compareTo(segmentLB) < 0 || (thisLB.compareTo(segmentLB) == 0 && thisLB.inclusive)) {
                currentSegment = segmentJoin(currentSegment, thisSegment, result);
                thisPtr++;
            }
            if (thisLB.compareTo(segmentLB) > 0 || (thisLB.compareTo(segmentLB) == 0)) {
                currentSegment = segmentJoin(currentSegment, segment, result);
                setPtr++;
            }
        }
        if (currentSegment != null) {
            result.add(currentSegment);
        }
        return result;
    }

    /**
     * Crosses this set with the passed.
     *
     * @param set what to cross with
     * @return intersection of sets
     */
    public RealNumbersSet intersection(RealNumbersSet set) {
        int thisPtr = 0;
        int setPtr = 0;
        RealNumbersSet result = new RealNumbersSet();
        Segment currentSegment = null;
        while (thisPtr != segments.size() && setPtr != set.segments.size()) {
            Segment thisSegment = thisPtr >= segments.size() ? INF : segments.get(thisPtr);
            Segment segment = setPtr >= set.segments.size() ? INF : set.segments.get(setPtr);

            Bound thisUB = thisSegment.getUpperBound();
            Bound segmentUB = segment.getUpperBound();

            currentSegment = segmentJoin(currentSegment, thisSegment.intersection(segment), result);
            if (thisUB.compareTo(segmentUB) < 0) {
                thisPtr++;
            }
            if (thisUB.compareTo(segmentUB) > 0) {
                setPtr++;
            }
            if (thisUB.compareTo(segmentUB) == 0) {
                thisPtr++;
                setPtr++;
            }
        }
        if (currentSegment != null) {
            result.add(currentSegment);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for (int i = 0; i < segments.size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(segments.get(i).toString());
        }
        sb.append(" }");
        return sb.toString();
    }
}
