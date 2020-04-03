import converter.CallChainToFilterMap;
import expression.Expression;
import parser.ArrayOperations;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String expression = scanner.nextLine();

        List<Expression> callChains = ArrayOperations.parse("filter{(element>0)}%>%filter{(element<0)}%>%map{(element*element)}");
        String result = CallChainToFilterMap.convert(callChains);

        System.out.println(result);
    }
}
