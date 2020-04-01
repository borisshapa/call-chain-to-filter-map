import parser.ArrayOperations;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String expression = scanner.nextLine();
        Object result = ArrayOperations.parse(expression);
        System.out.println(result);
    }
}
