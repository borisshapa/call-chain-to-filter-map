package set.bound;

public class Point extends Bound {
    public Integer number;

    public Point(Integer number, Boolean inclusive) {
        super(inclusive);
        this.number = number;
    }

    @Override
    public String toString() {
        return number.toString();
    }
}
