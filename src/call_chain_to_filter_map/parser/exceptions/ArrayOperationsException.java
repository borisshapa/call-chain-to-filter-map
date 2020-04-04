package call_chain_to_filter_map.parser.exceptions;

/**
 * Error in incorrect sequence format. See grammar.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class ArrayOperationsException extends RuntimeException {
    public ArrayOperationsException(final String message) {
        super(message);
    }
}
