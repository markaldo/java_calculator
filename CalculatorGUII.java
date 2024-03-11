package final_p1;

import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

import javax.swing.*;

public class Calculator extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel panel;
    private JTextField textField;
    private JButton[] buttons;
    private String[] buttonLabels = { "Num", "C", "<-", "x", "7", "8", "9", "/", "4", "5", "6", "+", "1", "2", "3", "-", "0", ".", "=", "^", "History", "Bin", "Hex", "Oct" };
    private int currentOperation;
    private String currentInput = "";
    private String resultOnScreen = "";

    public Calculator() {
        this.setTitle("Calculator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 300);
        panel = new JPanel();
        textField = new JTextField(20);
        panel.setLayout(new GridLayout(0, 4));
        buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
        }
        this.add(textField, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        
        String buttonLabel = event.getActionCommand();
        if (buttonLabel.equals("C")) {
            currentInput = "";
            textField.setText(currentInput);
        } else if (buttonLabel.equals("=")) {
            String result = evaluateExpression(currentInput);
            textField.setText(result);
            currentInput = result;
            // added this string
            resultOnScreen = result;
        } else if (resultOnScreen == currentInput && buttonLabel.matches("[0-9]")){
            // continue from here
            textField.setText("");
            currentInput += buttonLabel;
            textField.setText(currentInput);
        } else {
            // deal with this else
            currentInput += buttonLabel;
            textField.setText(currentInput);
        }
    }

    private String evaluateExpression(String expression) {
        Stack<Integer> operands = new Stack<Integer>();
        Stack<Character> operators = new Stack<Character>();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c)) {
                int num = c - '0';
                while (i + 1 < expression.length() && Character.isDigit(expression.charAt(i + 1))) {
                    num = num * 10 + (expression.charAt(i + 1) - '0');
                    i++;
                }
                operands.push(num);
            } else if (c == '+' || c == '-' || c == 'x' || c == '/' || c== '^') {
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    int result = performOperation(operands.pop(), operands.pop(), operators.pop());
                    operands.push(result);
                }
                operators.push(c);
            }
        }
        while (!operators.isEmpty()) {
            int result = performOperation(operands.pop(), operands.pop(), operators.pop());
            operands.push(result);
        }
        return String.valueOf(operands.pop());
    }

    private boolean hasPrecedence(char op1, char op2) {
        if ((op1 == '^' || op1 == 'x' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    private int performOperation(int operand2, int operand1, char operator) {
        switch (operator) {
        case '+':
            return operand1 + operand2;
        case '-':
            return operand1 - operand2;
        case 'x':
            return operand1 * operand2;
        case '/':
            if (operand2 == 0)
                throw new UnsupportedOperationException("Cannot divide by zero");
            return operand1 / operand2;
        case '^': 
            return (int) Math.pow(operand1,operand2);
        }
        return 0;
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
