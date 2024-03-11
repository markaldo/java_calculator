package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private TextView solutionTextView;
    private ArrayList<String> history;
    private String currentNumber;
    private boolean hasOperator;
    private int currentResult;
    private boolean hasCurrentResult;
    private boolean isDecimalMode;
    private boolean isHexadecimalMode;
    private boolean isOctalMode;
    private boolean isBinaryMode;
    private boolean isDecimalPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);
        solutionTextView = findViewById(R.id.solutionTextView);
        history = new ArrayList<>();
        currentNumber = "";
        hasCurrentResult = false;
        hasOperator = false;
        isDecimalMode = true;
        isBinaryMode = false;
        isHexadecimalMode = false;
        isOctalMode = false;

        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> showHistoryDialog());
    }

    public void onNumberClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        if (hasCurrentResult) {
            currentNumber = currentResult + buttonText;
        }
        hasOperator = false;
        currentNumber += buttonText;
        updateResultTextView();
    }

    public void onOperatorClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        if (!hasOperator) {
            if (currentNumber != null) {
                currentNumber += buttonText;
            }else{
                currentNumber = "0"+buttonText;
            }
        }else{
            solutionTextView.setText("");
            currentNumber = resultTextView.getText().toString();
            currentNumber += buttonText;
        }
        hasOperator = true;
        hasCurrentResult = false;
        updateResultTextView();
    }

    public void onPowerClick(View view) {
        if (!hasOperator) {
            if (!Objects.equals(currentNumber, "")) {
                currentNumber += "^";
            }else{
                currentNumber = "0^";
            }
        }else{
            solutionTextView.setText("");
            currentNumber = String.valueOf(currentResult);
            currentNumber += "^";
        }
        hasOperator = true;
        hasCurrentResult = false;
        updateResultTextView();
    }

    public void onEqualsClick(View view) {
        if (resultTextView != null) {
            String result = evaluateExpression(currentNumber);
            currentResult = Integer.parseInt(result);

            hasCurrentResult = true;
            hasOperator = true;

            history.add(currentNumber+" = "+currentResult+"\n");
            String expression = currentNumber+"=";
            solutionTextView.setText(expression);
            currentNumber = "";
            updateResultTextView();
        }
    }

    private String evaluateExpression(String expression) {
        Stack<Integer> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c)) {
                int num = c - '0';
                while (i + 1 < expression.length() && Character.isDigit(expression.charAt(i + 1))) {
                    num = num * 10 + (expression.charAt(i + 1) - '0');
                    i++;
                }
                operands.push(num);
            } else if (c == '+' || c == '-' || c == 'x' || c == '÷' || c== '^') {
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
        return (op1 != '^' && op1 != 'x' && op1 != '÷') || (op2 != '+' && op2 != '-');
    }

    private int performOperation(int operand2, int operand1, char operator) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case 'x':
                return operand1 * operand2;
            case '÷':
                if (operand2 == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");
                return operand1 / operand2;
            case '^':
                return (int) Math.pow(operand1,operand2);
        }
        return 0;
    }

    public void onBinaryClick(View view) {
        if (isDecimalMode) {
            // Convert decimal to binary
            decimalToBinary(view);
            isDecimalMode = false;
            isBinaryMode = true;
        } else if (isHexadecimalMode) {
            // Convert hexadecimal to binary
            hexadecimalToBinary(view);
            isHexadecimalMode = false;
            isBinaryMode = true;
        } else if (isOctalMode) {
            // Convert octal to binary
            octalToBinary(view);
            isOctalMode = false;
            isBinaryMode = true;
        } else if (isBinaryMode) {
            // Convert binary to decimal
            binaryToDecimal(view);
            isBinaryMode = false;
            isDecimalMode = true;
        }
    }

    public void onHexadecimalClick(View view) {
        if (isDecimalMode) {
            // Convert decimal to hexadecimal
            decimalToHexadecimal(view);
            isDecimalMode = false;
            isHexadecimalMode = true;
        } else if (isHexadecimalMode) {
            // Go to Decimal
            isDecimalMode = true;
            isHexadecimalMode = false;
            hexadecimalToDecimal(view);
        } else if (isOctalMode) {
            // Convert octal to hexadecimal
            octalToHexadecimal(view);
            isOctalMode = false;
            isHexadecimalMode = true;
        } else if (isBinaryMode) {
            // Convert binary to hexadecimal
            binaryToHexadecimal(view);
            isBinaryMode = false;
            isHexadecimalMode = true;
        }
    }

    public void onOctalClick(View view) {
        if (isDecimalMode) {
            // Convert decimal to octal
            decimalToOctal(view);
            isDecimalMode = false;
            isOctalMode = true;
        } else if (isHexadecimalMode) {
            // Convert hexadecimal to octal
            hexadecimalToOctal(view);
            isHexadecimalMode = false;
            isOctalMode = true;
        } else if (isOctalMode) {
            // Do nothing
            octalToDecimal(view);
            isDecimalMode = true;
            isOctalMode = false;
        } else if (isBinaryMode) {
            // Convert binary to octal
            binaryToOctal(view);
            isBinaryMode = false;
            isOctalMode = true;
        }
    }


    public void decimalToBinary(View view) {
        if (hasCurrentResult) {
            clearCurrentResult();
            if(Objects.equals(currentNumber, "")){
                currentNumber = String.valueOf(currentResult);
            }
        }
        int number = Integer.parseInt(currentNumber);
        currentNumber = Integer.toBinaryString(number);
        updateResultTextView();
    }

    public void decimalToOctal(View view) {
        if (hasCurrentResult) {
            clearCurrentResult();
            if(Objects.equals(currentNumber, "")){
                currentNumber = String.valueOf(currentResult);
            }
        }
        int number = Integer.parseInt(currentNumber);
        currentNumber = Integer.toOctalString(number);
        updateResultTextView();
    }

    public void decimalToHexadecimal(View view) {
        if (hasCurrentResult) {
            clearCurrentResult();
            if(Objects.equals(currentNumber, "")){
                currentNumber = String.valueOf(currentResult);
            }
        }
        int number = Integer.parseInt(currentNumber);
        String hexString = Integer.toHexString(number);
        currentNumber = hexString.toUpperCase();
        updateResultTextView();
    }

    public void binaryToDecimal(View view) {
        if (hasCurrentResult) {
            clearCurrentResult();
            if(Objects.equals(currentNumber, "")){
                currentNumber = String.valueOf(currentResult);
            }
        }
        int decimalNumber = Integer.parseInt(currentNumber, 2);
        currentNumber = String.valueOf(decimalNumber);
        updateResultTextView();
    }

    public void binaryToOctal(View view) {
        if (hasCurrentResult) {
            clearCurrentResult();
            if(Objects.equals(currentNumber, "")){
                currentNumber = String.valueOf(currentResult);
            }
        }
        int decimalNumber = Integer.parseInt(currentNumber, 2);
        currentNumber = Integer.toOctalString(decimalNumber);
        updateResultTextView();
    }

    public void binaryToHexadecimal(View view) {
        if (hasCurrentResult) {
            clearCurrentResult();
            if(Objects.equals(currentNumber, "")){
                currentNumber = String.valueOf(currentResult);
            }
        }
        int decimalNumber = Integer.parseInt(currentNumber, 2);
        String hexString = Integer.toHexString(decimalNumber);
        currentNumber = hexString.toUpperCase();
        updateResultTextView();
    }

    public void hexadecimalToDecimal(View view) {
        if (hasCurrentResult) {
            clearCurrentResult();
            if(Objects.equals(currentNumber, "")){
                currentNumber = String.valueOf(currentResult);
            }
        }
        int decimalNumber = Integer.parseInt(currentNumber, 16);
        currentNumber = String.valueOf(decimalNumber);
        updateResultTextView();
    }

    public void hexadecimalToBinary(View view) {
        if (hasCurrentResult) {
            clearCurrentResult();
            if(Objects.equals(currentNumber, "")){
                currentNumber = String.valueOf(currentResult);
            }
        }
        int decimalNumber = Integer.parseInt(currentNumber, 16);
        currentNumber = Integer.toBinaryString(decimalNumber);
        updateResultTextView();
    }

    public void hexadecimalToOctal(View view) {
        if (hasCurrentResult) {
            clearCurrentResult();
            if(Objects.equals(currentNumber, "")){
                currentNumber = String.valueOf(currentResult);
            }
        }
        int decimalNumber = Integer.parseInt(currentNumber, 16);
        currentNumber = Integer.toOctalString(decimalNumber);
        updateResultTextView();
    }

    public void octalToDecimal(View view) {
        if (hasCurrentResult) {
            clearCurrentResult();
            if(Objects.equals(currentNumber, "")){
                currentNumber = String.valueOf(currentResult);
            }
        }
        int decimalNumber = Integer.parseInt(currentNumber, 8);
        currentNumber = String.valueOf(decimalNumber);
        updateResultTextView();
    }

    public void octalToBinary(View view) {
        if (hasCurrentResult) {
            clearCurrentResult();
            if(Objects.equals(currentNumber, "")){
                currentNumber = String.valueOf(currentResult);
            }
        }
        int decimalNumber = Integer.parseInt(currentNumber, 8);
        currentNumber = Integer.toBinaryString(decimalNumber);
        updateResultTextView();
    }

    public void octalToHexadecimal(View view) {
        if (hasCurrentResult) {
            clearCurrentResult();
            if(Objects.equals(currentNumber, "")){
                currentNumber = String.valueOf(currentResult);
            }
        }
        int decimalNumber = Integer.parseInt(currentNumber, 8);
        String hexString = Integer.toHexString(decimalNumber);
        currentNumber = hexString.toUpperCase();
        updateResultTextView();
    }

    public void onDecimalClick(View view) {
        if (!isDecimalPressed) {
            isDecimalPressed = true;

            if (hasCurrentResult) {
                currentNumber = "0.";
                hasCurrentResult = false;
            } else {
                currentNumber += ".";
            }

            updateResultTextView();
        }
    }

    public void onDeleteClick(View view) {
        if (currentNumber.isEmpty()) {
           String screen = resultTextView.getText().toString();
           currentNumber = screen.substring(0, screen.length() - 1);
        } else {
            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
        }
        updateResultTextView();
    }

    public void clearCurrentResult() {
        currentNumber = "";
        hasOperator = false;
        hasCurrentResult = false;
        solutionTextView.setText("0");
        updateResultTextView();
    }

    public void onClearClick(View view) {
        clearCurrentResult();
    }

    public void onResetClick(View view) {
        currentNumber = "";
        hasOperator = false;
        hasCurrentResult = false;
        history.clear();
        solutionTextView.setText("0");
        updateResultTextView();
    }

    private void updateResultTextView() {
        String text = "";
        if (!currentNumber.isEmpty()) {
            text += currentNumber;
        }
        else if (hasCurrentResult) {
            text += String.valueOf(currentResult);
        }else {
            text += "0";
        }
        resultTextView.setText(text);
    }

    private void showHistoryDialog() {
        // Creating a new dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.history_dialog);

        // Setting the dialog title
        dialog.setTitle("History");

        // Getting the TextView in the dialog
        TextView historyTextView = dialog.findViewById(R.id.historyTextView);

        // Setting the text of the TextView to the history list
        StringBuilder sb = new StringBuilder();
        for (String item : history) {
            sb.append(item).append("\n");
        }
        historyTextView.setText(sb.toString());

        // Showing the dialog
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        // Dismiss the dialog if it's showing
        Dialog dialog = new Dialog(this);
        if (dialog.isShowing()) {
            dialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }

}