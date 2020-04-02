package set;

import org.junit.Assert;
import org.junit.Test;
import set.bound.Point;

public class RealNumberSetTest {
    private static final Segment POINT_0 = new Segment(new Point(0, true), new Point(0, true));
    public static final Segment POINT_7 = new Segment(new Point(7, true), new Point(7, true));

    private void valid(final RealNumbersSet set, final String expected) {
        Assert.assertEquals(expected, set.toString());
    }

    @Test
    public void point() {
        valid(new RealNumbersSet(POINT_0), "{ [0 .. 0] }");
    }

    @Test
    public void pointsUnion() {
        RealNumbersSet point0 = new RealNumbersSet(POINT_0);
        RealNumbersSet point7 = new RealNumbersSet(POINT_7);
        valid(point0.union(point7), "{ [0 .. 0], [7 .. 7] }");
    }

    @Test
    public void pointsInersection() {
        RealNumbersSet point0 = new RealNumbersSet(POINT_0);
        RealNumbersSet point7 = new RealNumbersSet(POINT_7);
        valid(point0.intersection(point7), "{  }");
    }
}
