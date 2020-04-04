package call_chain_to_filter_map.parser.exceptions;

/**
 * Incorrect syntax error. See grammar.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class InvalidSyntaxException extends ArrayOperationsException {
    public InvalidSyntaxException(String message) {
        super("SYNTAX ERROR: " + message);
    }
}
