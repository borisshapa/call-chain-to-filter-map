package set;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

public class Segment {
    private Bound lowerBound;
    private Bound upperBound;

    public Segment(Bound lowerBound, Bound upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

        if (lowerBound.compareTo(upperBound) > 0) {
            throw new IllegalArgumentException("The lower boundary of the segment cannot be to the right of the upper");
        }
    }

    public Segment(Double lowerBound, Double upperBound) {
        this(new Bound(lowerBound), new Bound(upperBound));
    }

    public Segment(Double lowerBound, Bound upperBound) {
        this(new Bound(lowerBound), upperBound);
    }

    public Segment(Bound lowerBound, Double upperBound) {
        this(lowerBound, new Bound(upperBound));
    }

    public Segment(Double point) {
        this(new Bound(point), new Bound(point));
    }

    public Bound getLowerBound() {
        return lowerBound;
    }

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
