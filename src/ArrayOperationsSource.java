/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public interface ArrayOperationsSource {
    boolean hasNext();
    char next();
    ArrayOperationsException error(final String message);
}
