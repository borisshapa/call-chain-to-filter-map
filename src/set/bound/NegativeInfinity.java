package set.bound;

public class NegativeInfinity extends Bound {
    public NegativeInfinity() {
        super(false);
    }

    @Override
    public String toString() {
        return "-inf";
    }
}
