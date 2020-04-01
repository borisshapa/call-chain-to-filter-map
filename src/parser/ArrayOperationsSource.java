package parser;

import parser.exceptions.ArrayOperationsException;

/**
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public interface ArrayOperationsSource {
    char END = '\0';
    boolean hasNext();
    char next();
    ArrayOperationsException error(final String message);
}
