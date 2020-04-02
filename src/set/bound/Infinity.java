package set.bound;

public class Infinity extends Bound {
    public Infinity() {
        super(false);
    }

    @Override
    public String toString() {
        return "inf";
    }
}
