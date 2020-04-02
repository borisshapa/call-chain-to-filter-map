package parser;

import exceptions.ArrayOperationsException;

/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class BaseParser {
    private final ArrayOperationsSource source;
    protected char ch;

    protected BaseParser(final ArrayOperationsSource source) {
        this.source = source;
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : '\0';
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected void expect(final char c) {
        if (ch != c) {
            throw error("Expected '" + c + "', found '" + ch + "'");
        }
        nextChar();
    }

    protected void expect(final String value) {
        for (char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected void expectType(Object obj, Class<?> token) {
        Class<?> objClass = obj.getClass();
        if (!token.isAssignableFrom(objClass)) {
            throw error("Expected expression type " + token.getName() + ", found " + objClass);
        }
    }

    protected ArrayOperationsException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
