package call_chain_to_filter_map;

import call_chain_to_filter_map.converter.CallChainToFilterMap;
import call_chain_to_filter_map.expression.Expression;
import call_chain_to_filter_map.parser.ArrayOperations;

import java.util.List;
import java.util.Scanner;

/**
 * The main class for running the program.
 * It takes one line to the standard input stream - the expression
 * described by the {@code <call-chain>} rule and output a line with the converted expression to {@code <filter>%>%<map>} form.
 *
 * @author <a href="https://teleg.run/borisshapa">Boris Shaposhnikov</a>
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();

        List<Expression> callChains = ArrayOperations.parse(expression);
        String result = CallChainToFilterMap.convert(callChains);

        System.out.println(result);
    }
}
