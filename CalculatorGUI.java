package final_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class CalculatorGUI extends JFrame {

    private JTextField textField;
    private String operation;
    private double prevResult;
    private ArrayList<String> inputList;

    public CalculatorGUI() {
        // create GUI components
        textField = new JTextField();
        JPanel btnPanel = new JPanel(new GridLayout(4, 3));

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        JButton openParenthesisBtn = new JButton("(");
        openParenthesisBtn.addActionListener(new ParenthesisButtonListener("("));

        JButton closeParenthesisBtn = new JButton(")");
        closeParenthesisBtn.addActionListener(new ParenthesisButtonListener(")"));

        JButton divisionBtn = new JButton("/");
        divisionBtn.addActionListener(new OperationButtonListener("/"));

        JButton multiplicationBtn = new JButton("x");
        multiplicationBtn.addActionListener(new OperationButtonListener("x"));

        JButton subtractionBtn = new JButton("-");
        subtractionBtn.addActionListener(new OperationButtonListener("-"));

        JButton additionBtn = new JButton("+");
        additionBtn.addActionListener(new OperationButtonListener("+"));

        JButton powerBtn = new JButton("^");
        powerBtn.addActionListener(new OperationButtonListener("^"));

        JButton equalsBtn = new JButton("=");
        equalsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performCalculation();
            }
        });

        JButton dotBtn = new JButton(".");
        dotBtn.addActionListener(new NumberButtonListener("."));

        JButton zeroBtn = new JButton("0");
        zeroBtn.addActionListener(new NumberButtonListener("0"));

        JButton oneBtn = new JButton("1");
        oneBtn.addActionListener(new NumberButtonListener("1"));

        JButton twoBtn = new JButton("2");
        twoBtn.addActionListener(new NumberButtonListener("2"));

        JButton threeBtn = new JButton("3");
        threeBtn.addActionListener(new NumberButtonListener("3"));

        JButton fourBtn = new JButton("4");
        fourBtn.addActionListener(new NumberButtonListener("4"));

        JButton fiveBtn = new JButton("5");
        fiveBtn.addActionListener(new NumberButtonListener("5"));

        JButton sixBtn = new JButton("6");
        sixBtn.addActionListener(new NumberButtonListener("6"));

        JButton sevenBtn = new JButton("7");
        sevenBtn.addActionListener(new NumberButtonListener("7"));

        JButton eightBtn = new JButton("8");
        eightBtn.addActionListener(new NumberButtonListener("8"));

        JButton nineBtn = new JButton("9");
        nineBtn.addActionListener(new NumberButtonListener("9"));


        // add components to button panel
        btnPanel.add(clearBtn);
        btnPanel.add(openParenthesisBtn);
        btnPanel.add(closeParenthesisBtn);
        btnPanel.add(powerBtn);
        btnPanel.add(sevenBtn);
        btnPanel.add(eightBtn);
        btnPanel.add(nineBtn);
        btnPanel.add(divisionBtn);
        btnPanel.add(fourBtn);
        btnPanel.add(fiveBtn);
        btnPanel.add(sixBtn);
        btnPanel.add(multiplicationBtn);
        btnPanel.add(oneBtn);
        btnPanel.add(twoBtn);
        btnPanel.add(threeBtn);
        btnPanel.add(subtractionBtn);
        btnPanel.add(zeroBtn);
        btnPanel.add(dotBtn);
        btnPanel.add(equalsBtn);
        btnPanel.add(additionBtn);

        // add components to main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textField, BorderLayout.NORTH);
        mainPanel.add(btnPanel, BorderLayout.CENTER);

        // set main frame properties and add panel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Calculator");
        getContentPane().add(mainPanel);
        pack();
        setVisible(true);

        prevResult = 0.0;
        inputList = new ArrayList<>();
    }

    public void clear() {
        textField.setText("");
        inputList.clear();
        prevResult = 0.0;
        operation = "";
    }

    public void performCalculation() {
        String inputString = textField.getText();
        double result = evaluateExpression(inputString);
        textField.setText(String.valueOf(result));
        prevResult = result;
        inputList.clear();
        inputList.add(textField.getText());
    }

    private void addInputToList(String s) {
        inputList.add(s);
    }

    private double peekNextOperand() {
        if (inputList.size() > 1 && inputList.get(1).equals("(")) {
            return evaluateSubExpression();
        }

        return Double.parseDouble(inputList.get(1));
    }

    private double evaluateExpression(String s) {
        inputList.clear();
        String[] tokens = s.split("(?<=[^\\d.])(?=[\\d.])|(?<=[\\d.])(?=[^\\d.])"); // using regex to split string into token array
        Collections.addAll(inputList, tokens);

        return evaluateSubExpression();
    }

    private double evaluateSubExpression() {
        if (inputList.isEmpty()) {
            throw new IllegalArgumentException("No input");
        }
    
        double leftOperand = 0;
        double rightOperand = 0;
        String operator = "";
        ArrayList<String> operands = new ArrayList<>();
        ArrayList<String> operators = new ArrayList<>();
    
        // Separate operands and operators
        while (!inputList.isEmpty()) {
            String token = inputList.remove(0);
            if (isNumber(token)) {
                operands.add(token);
            } else if (isOperator(token)) {
                operators.add(token);
            } else {
                throw new IllegalArgumentException("Invalid input: " + token);
            }
        }
    
        // Process multiplication and division first
        for (int i = 0; i < operators.size(); i++) {
            String op = operators.get(i);
            if (op.equals("x") || op.equals("/")) {
                leftOperand = Double.parseDouble(operands.get(i));
                rightOperand = Double.parseDouble(operands.get(i + 1));
                double result = applyOperator(leftOperand, rightOperand, op);
                operands.set(i + 1, String.valueOf(result));
                operands.remove(i);
                operators.remove(i--);
            }
        }
    
        // Process addition and subtraction next
        for (int i = 0; i < operators.size(); i++) {
            String op = operators.get(i);
            if (op.equals("+") || op.equals("-")) {
                leftOperand = Double.parseDouble(operands.get(i));
                rightOperand = Double.parseDouble(operands.get(i + 1));
                double result = applyOperator(leftOperand, rightOperand, op);
                operands.set(i + 1, String.valueOf(result));
                operands.remove(i);
                operators.remove(i--);
            }
        }
    
        // There should be only one operand left in the list
        if (operands.size() != 1) {
            throw new IllegalArgumentException("Invalid input");
        }
    
        return Double.parseDouble(operands.get(0));
    }
    
    private boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("x") || s.equals("/");
    }
    

    private double applyOperator(double operand1, double operand2, String operator) {
        double result = 0.0;

        switch (operator) {
            case "+":
                result = operand1 + operand2;
                break;
            case "-":
                result = operand1 - operand2;
                break;
            case "x":
                result = operand1 * operand2;
                break;
            case "/":
                result = operand1 / operand2;
                break;
        }

        return result;
    }

    private class NumberButtonListener implements ActionListener {
        private String num;

        NumberButtonListener(String num) {
            this.num = num;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (textField.getText().equals(String.valueOf(prevResult))) {
                // If the input field contains the previous result, clear it
                clear();
            }
            textField.setText(textField.getText() + num);
            addInputToList(num);
        }
    }

    private class ParenthesisButtonListener implements ActionListener {
        private String parenthesis;

        ParenthesisButtonListener(String parenthesis) {
            this.parenthesis = parenthesis;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            textField.setText(textField.getText() + parenthesis);
            addInputToList(parenthesis);
        }
    }

    private class OperationButtonListener implements ActionListener {
        private String op;

        OperationButtonListener(String op) {
            this.op = op;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (inputList.isEmpty()) {
                return; // do nothing if inputList is empty
            }

            String lastToken = inputList.get(inputList.size() - 1);

            if (lastToken.equals("(")) {
                return; // do nothing if last token is an opening parenthesis
            }

            textField.setText(textField.getText() + op);
            addInputToList(op);
        }
    }

    public static void main(String[] args) {
        CalculatorGUI gui = new CalculatorGUI();
    }
}



