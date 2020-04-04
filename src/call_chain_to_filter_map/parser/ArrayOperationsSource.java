package call_chain_to_filter_map.parser;

import call_chain_to_filter_map.parser.exceptions.InvalidSyntaxException;
import call_chain_to_filter_map.parser.exceptions.UnsupportedTypeException;

/**
 * Source where to get characters.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public interface ArrayOperationsSource {
    /**
     * End of array operations sequence.
     */
    char END = '\0';

    /**
     * Is there a next character.
     *
     * @return <var>true</var> if and only if there is a next character, and <var>false</var> otherwise
     */
    boolean hasNext();

    /**
     * Returns a next character.
     *
     * @return next character
     */
    char next();

    /**
     * Syntax error.
     *
     * @param message information about error
     * @return {@link InvalidSyntaxException}
     */
    InvalidSyntaxException syntaxError(final String message);

    /**
     * Type error.
     *
     * @param message information about error
     * @return {@link InvalidSyntaxException}
     */
    UnsupportedTypeException typeError(final String message);
}
