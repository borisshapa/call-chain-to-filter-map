package call_chain_to_filter_map.parser;

import call_chain_to_filter_map.parser.exceptions.InvalidSyntaxException;
import call_chain_to_filter_map.parser.exceptions.UnsupportedTypeException;

/**
 * Parser of characters from {@link ArrayOperationsSource}.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class BaseParser {
    private final ArrayOperationsSource source;
    /**
     * currently character.
     */
    protected char ch;

    /**
     * Constructs BaseParser for <var>source</var> parsing.
     *
     * @param source what to parse
     */
    protected BaseParser(final ArrayOperationsSource source) {
        this.source = source;
    }

    /**
     * Moves the current character pointer to the next if the next exists, otherwise it sets <var>ch</var> to '\0'.
     */
    protected void nextChar() {
        ch = source.hasNext() ? source.next() : '\0';
    }

    /**
     * If the current character matches the expected, changes the current character pointer to the next.
     *
     * @param expected expected character
     * @return <var>true</var> if and only if the currently character matches the <var>expected</var>, false otherwise.
     */
    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    /**
     * If the current character does not match the expected one throws {@link InvalidSyntaxException},
     * otherwise it moves the current character pointer to the next.
     *
     * @param c expected character
     */
    protected void expect(final char c) {
        if (ch != c) {
            throw syntaxError("Expected '" + c + "', found '" + ch + "'");
        }
        nextChar();
    }

    /**
     * If the expected line does not match any prefix suffix of the source,
     * throws {@link InvalidSyntaxException}, otherwise it moves the pointer to the current character
     * by the length of the expected {@link String} to the right.
     *
     * @param value expected {@link String}
     */
    protected void expect(final String value) {
        for (char c : value.toCharArray()) {
            expect(c);
        }
    }

    /**
     * Throws {@link UnsupportedTypeException} if and only if the class of the <var>obj</var> does not match the <var>token</var>.
     *
     * @param obj what to check
     * @param token expected type
     */
    protected void expectType(Object obj, Class<?> token) {
        Class<?> objClass = obj.getClass();
        if (!token.isAssignableFrom(objClass)) {
            throw typeError("Expected call_chain_to_filter_map.expression type " + token.getName() + ", found " + objClass.getName());
        }
    }

    /**
     * Returns {@link InvalidSyntaxException} with the message passed.
     *
     * @param message error information
     * @return {@link InvalidSyntaxException}
     */
    protected InvalidSyntaxException syntaxError(final String message) {
        return source.syntaxError(message);
    }

    /**
     * Returns {@link UnsupportedTypeException} with the message passed.
     *
     * @param message error information
     * @return {@link UnsupportedTypeException}
     */
    protected UnsupportedTypeException typeError(final String message) { return source.typeError(message); }

    /**
     * Checks if the current character is in the specified range.
     *
     * @param from the lower bound of the range
     * @param to the upper bound of the range
     * @return <var>true</var> if and only if the current character hits the border, including borders, <var>false</var> otherwise.
     */
    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
