/**
 * <call-chain> string parsing.
 * <pre> {@code
 * <ws> ::= "" | '0020' | '000A' | '000D' | '0009'
 * <ws-seq> ::= <ws> | <ws> <ws-seq>
 * <digit> ::= “0” | “1" | “2” | “3" | “4” | “5" | “6” | “7" | “8” | “9"
 * <number> ::= <digit> | <digit> <number>
 * <operation> ::= “+” | “-” | “*” | “>” | “<” | “=” | “&” | “|”
 * <constant-expression> ::= “-” <number> | <number>
 * <binary-expression> ::= “(” <expression> <operation> <expression> “)”
 * <weak-expression> ::= “element” | <constant-expression> | <binary-expression>
 * <expression> ::= <ws-seq> <weak-expression> <ws-seq>
 * <map-call> ::= “map{” <expression> “}”
 * <filter-call> ::= “filter{” <expression> “}”
 * <weak-call> ::= <map-call> | <filter-call>
 * <call> ::= <ws-seq> <weak-call> <ws-seq>
 * <call-chain> ::= <call> | <call> “%>%” <call-chain>}
 * </pre>
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
package call_chain_to_filter_map.parser;