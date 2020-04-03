package set;

import java.util.ArrayList;
import java.util.List;

public class RealNumbersSet {
    private final static Segment INF = new Segment(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

    private List<Segment> segments;

    public RealNumbersSet(Segment segment) {
        this();
        add(segment);
    }

    public RealNumbersSet(Double point) {
        this(new Segment(point, point));
    }

    public RealNumbersSet(Double bound1, Double bound2) {
        this(new Bound(bound1), new Bound(bound2));
    }

    public RealNumbersSet() {
        segments = new ArrayList<>();
    }

    public RealNumbersSet(Bound bound1, Bound bound2) {
        this(new Segment(bound1, bound2));
    }

    public RealNumbersSet(List<Segment> segments) {
        this.segments = segments;
    }

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
