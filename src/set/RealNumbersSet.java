package set;

import set.bound.Bound;
import set.bound.Infinity;
import set.bound.Point;

import java.util.ArrayList;
import java.util.List;

public class RealNumbersSet {
    private final static Segment INF = new Segment(new Infinity(), new Infinity());

    private List<Segment> segments;

    public RealNumbersSet(Segment segment) {
        segments = new ArrayList<>(List.of(segment));
    }

    public RealNumbersSet() {
        segments = new ArrayList<>();
    }

    public RealNumbersSet(List<Segment> segments) {
        this.segments = segments;
    }

    private void add(Segment segment) {
        segments.add(segment);
    }

    private Segment segmentJoin(Segment accumulatedSegment, Segment addedSegment, RealNumbersSet generatedSet) {
        if (accumulatedSegment == null) {
            return addedSegment;
        } else if (accumulatedSegment.union(addedSegment) == null) {
            generatedSet.add(accumulatedSegment);
            return addedSegment;
        }
        return accumulatedSegment;
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

    public static void main(String[] args) {
        Segment segment1 = new Segment(new Point(7, true), new Point(7, true));
        Segment segment2 = new Segment(new Point(7, false), new Point(9, false));

    }
}
