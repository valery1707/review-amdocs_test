import java.util.Stack;
import java.util.function.Predicate;

public class Eval {

    public static int evaluate(String expression) {
        char[] tokens = expression.toCharArray();

        // Stack for numbers: 'values'
        Stack<Integer> values = new Stack<Integer>();
        // Stack for Operators: 'ops'
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] >= '0' && tokens[i] <= '9') {
                // Current token is a number, push it to stack for numbers
                int start = i;
                do {
                    i++;
                } while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9');
                values.push(Integer.valueOf(expression.substring(start, i--)));
            } else if (tokens[i] == '(') {
                // Current token is an opening brace, push it to 'ops'
                ops.push(tokens[i]);
            } else if (tokens[i] == ')') {
                // Closing brace encountered, solve entire brace
                applyOps(ops, values, op -> op != '(');
                ops.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                // Current token is an operator.
                applyOps(ops, values, Eval::hasPrecedence);
                ops.push(tokens[i]);
            }
        }

        // Entire expression has been parsed at this point, apply remaining ops to remaining values
        applyOps(ops, values, __ -> true);

        return values.pop();
    }

    // Returns true if 'op2' has higher or same precedence as 'op1', otherwise returns false.
    public static boolean hasPrecedence(char op2) {
        return op2 == '*' || op2 == '/';
    }

    private static void applyOps(Stack<Character> ops, Stack<Integer> values, Predicate<Character> opTest) {
        while (!ops.isEmpty() && opTest.test(ops.peek())) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }
    }

    // A utility method to apply an operator 'op' on operands 'a' and 'b'. Return the result.
    public static int applyOp(char op, int b, int a) {
        switch (op) {
            case '-':
                return a - b;
            case '+':
                return a + b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return a / b;
        }
        throw new UnsupportedOperationException("Invalid operator: " + op);
    }

}
