package call_chain_to_filter_map.parser;

import call_chain_to_filter_map.parser.exceptions.InvalidSyntaxException;
import call_chain_to_filter_map.parser.exceptions.UnsupportedTypeException;

/**
 * String where to get characters.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class StringSource implements ArrayOperationsSource {
    private final String data;
    private int pos;

    /**
     * Constructs a new StringSource, where to get characters.
     *
     * @param data string to get around.
     */
    public StringSource(final String data) {
        this.data = data + END;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public InvalidSyntaxException syntaxError(String message) {
        return new InvalidSyntaxException(pos + ": " + message);
    }

    @Override
    public UnsupportedTypeException typeError(String message) {
        return new UnsupportedTypeException(pos + ": " + message);
    }
}
