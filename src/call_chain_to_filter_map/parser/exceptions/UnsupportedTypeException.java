package call_chain_to_filter_map.parser.exceptions;

/**
 * Mismatch between the type of the transferred object and the expected type of the operation argument.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class UnsupportedTypeException extends ArrayOperationsException {
    public UnsupportedTypeException(String message) {
        super("TYPE ERROR: " + message);
    }
}
