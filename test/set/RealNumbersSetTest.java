package set;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class RealNumbersSetTest {
    private static final Map<String, Bound> POINTS = Map.of(
            "[0]", new Bound(0.0),
            "[7]", new Bound(7.0),
            "(0)", new Bound(0.0, false),
            "(7)", new Bound(7.0, false),
            "[5]", new Bound(5.0),
            "(5)", new Bound(5.0, false),
            "[10]", new Bound(10.0, true),
            "(10)", new Bound(10.0, false),
            "[3]", new Bound(3.0),
            "(3)", new Bound(3.0, false)
    );

    private static final Map<String, Segment> SEGMENTS_0_7 = Map.of(
            "[0 .. 7]", new Segment(POINTS.get("[0]"), POINTS.get("[7]")),
            "[0 .. 7)", new Segment(POINTS.get("[0]"), POINTS.get("(7)")),
            "(0 .. 7]", new Segment(POINTS.get("(0)"), POINTS.get("[7]")),
            "(0 .. 7)", new Segment(POINTS.get("(0)"), POINTS.get("(7)"))
    );

    public static final Map<String, Segment> SEGMENT_5_10 = Map.of(
            "[5 .. 10]", new Segment(POINTS.get("[5]"), POINTS.get("[10]")),
            "[5 .. 10)", new Segment(POINTS.get("[5]"), POINTS.get("(10)")),
            "(5 .. 10]", new Segment(POINTS.get("(5)"), POINTS.get("[10]")),
            "(5 .. 10)", new Segment(POINTS.get("(5)"), POINTS.get("(10)"))
    );

    public static final Map<String, Segment> SEGMENT_0_5 = Map.of(
            "[0 .. 5]", new Segment(POINTS.get("[0]"), POINTS.get("[5]")),
            "[0 .. 5)", new Segment(POINTS.get("[0]"), POINTS.get("(5)")),
            "(0 .. 5]", new Segment(POINTS.get("(0)"), POINTS.get("[5]")),
            "(0 .. 5)", new Segment(POINTS.get("(0)"), POINTS.get("(5)"))
    );

    private void valid(final RealNumbersSet set, final String expected) {
        Assert.assertEquals(expected, set.toString());
    }

    @Test
    public void point() {
        valid(new RealNumbersSet(0.0), "{ [0.0 .. 0.0] }");
    }

    @Test
    public void pointsUnion() {
        RealNumbersSet point0 = new RealNumbersSet(0.0);
        RealNumbersSet point7 = new RealNumbersSet(7.0);
        valid(point0.union(point7), "{ [0.0 .. 0.0], [7.0 .. 7.0] }");
    }

    @Test
    public void pointsInersection() {
        RealNumbersSet point0 = new RealNumbersSet(0.0);
        RealNumbersSet point7 = new RealNumbersSet(7.0);
        valid(point0.intersection(point7), "{  }");
    }

    @Test
    public void segment() {
        valid(new RealNumbersSet(SEGMENTS_0_7.get("[0 .. 7]")), "{ [0.0 .. 7.0] }");
    }

    @Test
    public void segmentAndBound() {
        RealNumbersSet point = new RealNumbersSet(7.0);
        RealNumbersSet segment = new RealNumbersSet(SEGMENTS_0_7.get("(0 .. 7)"));
        valid(point.union(segment), "{ (0.0 .. 7.0] }");
        valid(segment.union(point), "{ (0.0 .. 7.0] }");
    }

    @Test
    public void pointInsideSegment() {
        RealNumbersSet point1 = new RealNumbersSet(5.0);
        RealNumbersSet point2 = new RealNumbersSet(POINTS.get("(5)"), POINTS.get("(5)"));
        for (Map.Entry<String, Segment> segment : SEGMENTS_0_7.entrySet()) {
            RealNumbersSet segmentSet = new RealNumbersSet(segment.getValue());
            valid(segmentSet.union(point1), segmentSet.toString());
            valid(point1.union(segmentSet), segmentSet.toString());

            valid(segmentSet.union(point2), segmentSet.toString());
            valid(point2.union(segmentSet), segmentSet.toString());
        }
    }

    @Test
    public void emptyBound() {
        RealNumbersSet point = new RealNumbersSet(POINTS.get("(5)"), POINTS.get("(5)"));
        valid(point, "{  }");
    }

    @Test
    public void intersection() {
        for (Map.Entry<String, Segment> segment1 : SEGMENTS_0_7.entrySet()) {
            for (Map.Entry<String, Segment> segment2 : SEGMENT_5_10.entrySet()) {
                Segment seg1 = segment1.getValue();
                Segment seg2 = segment2.getValue();

                RealNumbersSet setSegment1 = new RealNumbersSet(seg1);
                RealNumbersSet setSegment2 = new RealNumbersSet(seg2);
                valid(setSegment1.intersection(setSegment2),
                        new RealNumbersSet(new Segment(seg2.getLowerBound(), seg1.getUpperBound())).toString());
            }
        }
    }

    @Test
    public void unionSameLB() {
        for (Map.Entry<String, Segment> segment1 : SEGMENT_0_5.entrySet()) {
            for (Map.Entry<String, Segment> segment2 : SEGMENTS_0_7.entrySet()) {
                Segment seg1 = segment1.getValue();
                Segment seg2 = segment2.getValue();

                RealNumbersSet setSegment1 = new RealNumbersSet(seg1);
                RealNumbersSet setSegment2 = new RealNumbersSet(seg2);
                valid(setSegment1.union(setSegment2),
                        new RealNumbersSet(
                                new Segment(
                                        new Bound(0.0,
                                                seg1.getLowerBound().inclusive | seg2.getLowerBound().inclusive),
                                        seg2.getUpperBound())).toString());
            }
        }
    }

    @Test
    public void intersectionSameLB() {
        for (Map.Entry<String, Segment> segment1 : SEGMENT_0_5.entrySet()) {
            for (Map.Entry<String, Segment> segment2 : SEGMENTS_0_7.entrySet()) {
                Segment seg1 = segment1.getValue();
                Segment seg2 = segment2.getValue();

                RealNumbersSet setSegment1 = new RealNumbersSet(seg1);
                RealNumbersSet setSegment2 = new RealNumbersSet(seg2);
                valid(setSegment1.intersection(setSegment2),
                        new RealNumbersSet(
                                new Segment(
                                        new Bound(0.0,
                                                seg1.getLowerBound().inclusive & seg2.getLowerBound().inclusive),
                                        seg1.getUpperBound())).toString());
            }
        }
    }

    @Test
    public void complicated1() {
        Segment seg1 = new Segment(POINTS.get("(0)"), POINTS.get("[5]"));
        Segment seg2 = new Segment(POINTS.get("[3]"), POINTS.get("[7]"));
        Segment seg3 = new Segment(POINTS.get("(7)"), POINTS.get("(10)"));
        Segment seg4 = new Segment(POINTS.get("[10]"), POINTS.get("[10]"));

        RealNumbersSet set1 = new RealNumbersSet(List.of(seg1, seg3));
        RealNumbersSet set2 = new RealNumbersSet(List.of(seg2, seg4));

        valid(set1.union(set2), "{ (0.0 .. 10.0] }");
        valid(set1.intersection(set2), "{ [3.0 .. 5.0] }");
    }

    @Test
    public void complicated2() {
        RealNumbersSet set1 = new RealNumbersSet(new Segment(Double.NEGATIVE_INFINITY, POINTS.get("[0]")));
        RealNumbersSet set2 = new RealNumbersSet(new Segment(POINTS.get("(0)"), Double.POSITIVE_INFINITY));

        valid(set1.union(set2), "{ (-Infinity .. Infinity) }");
        valid(set1.intersection(set2), "{  }");
    }

    @Test
    public void complicated3() {
        Segment seg1 = new Segment(Double.NEGATIVE_INFINITY, POINTS.get("[0]"));
        Segment seg2 = new Segment(POINTS.get("[3]"), POINTS.get("[3]"));
        Segment seg3 = new Segment(POINTS.get("(5)"), POINTS.get("[7]"));

        Segment seg4 = new Segment(POINTS.get("(0)"), POINTS.get("(3)"));
        Segment seg5 = new Segment(POINTS.get("(3)"), POINTS.get("(10)"));
        Segment seg6 = new Segment(POINTS.get("(10)"), Double.POSITIVE_INFINITY);

        RealNumbersSet set1 = new RealNumbersSet(List.of(seg1, seg2, seg3));
        RealNumbersSet set2 = new RealNumbersSet(List.of(seg4, seg5, seg6));

        valid(set1.union(set2), "{ (-Infinity .. 10.0), (10.0 .. Infinity) }");
        valid(set1.intersection(set2), "{ (5.0 .. 7.0] }");
    }
}
