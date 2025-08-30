import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class Calculator extends JFrame implements ActionListener {
     JTextField display;
     JPanel buttonPanel;
     double num1 = 0, num2 = 0, result = 0;
     char operator;
     boolean start = true;
     DecimalFormat df = new DecimalFormat("#.########");

     // color of window
     Color BACKGROUND_COLOR = new Color(32, 32, 32);
     Color DISPLAY_COLOR = new Color(0, 0, 0);
     Color BUTTON_COLOR = new Color(60, 60, 60);
     Color BUTTON_HOVER = new Color(80, 80, 80);
     Color OPERATOR_COLOR = new Color(0, 120, 212);
     Color OPERATOR_HOVER = new Color(40, 140, 232);
     Color EQUALS_COLOR = new Color(0, 120, 212);
     Color TEXT_COLOR = Color.WHITE;

    public Calculator() {

        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Create display
        createDisplay();
        
        // Create buttons
        createButtons();
        
        setVisible(true);
    }

 

    private void createDisplay() {
        display = new JTextField("0");
        display.setEditable(false);
        display.setBackground(DISPLAY_COLOR);
        display.setForeground(TEXT_COLOR);
        display.setFont(new Font("Segoi UI", Font.BOLD, 30));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        display.setPreferredSize(new Dimension(400, 120));
        
        add(display, BorderLayout.NORTH);
    }

    private void createButtons() {
        buttonPanel = new JPanel(new GridLayout(6, 4, 2, 2));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[][] buttons = 
        {
            {"%", "CE", "C", "⌫"},
            {"1/x", "x²", "²√x", "÷"},
            {"7", "8", "9", "×"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "+"},
            {"+/-", "0", ".", "="}
        };

        // range loop to take the row in Array
        for (String[] row : buttons) 
        {
            for (String text : row) 
            {
                JButton button = createStyledButton(text);
                buttonPanel.add(button);
            }
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

     JButton createStyledButton(String text) 
     {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setForeground(TEXT_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        
        if (isOperator(text) || text.equals("=")) 
        {
            button.setBackground(OPERATOR_COLOR);
            button.addMouseListener(new java.awt.event.MouseAdapter() 
            {
                public void mouseEntered(java.awt.event.MouseEvent evt) 
                {
                    button.setBackground(OPERATOR_HOVER);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) 
                {
                    button.setBackground(OPERATOR_COLOR);
                }
            });
        } 
        else {
            button.setBackground(BUTTON_COLOR);
            button.addMouseListener(new java.awt.event.MouseAdapter() 
            {
                public void mouseEntered(java.awt.event.MouseEvent evt) 
                {
                    button.setBackground(BUTTON_HOVER);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) 
                {
                    button.setBackground(BUTTON_COLOR);
                }
            });
        }

        button.addActionListener(this);
        return button;
    }

    private boolean isOperator(String text) {
        return text.equals("+") || text.equals("-") || text.equals("×") || 
               text.equals("÷") || text.equals("%") || text.equals("1/x") || 
               text.equals("x²") || text.equals("²√x");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        try {
            // Number
            if (command.matches("[0-9]")) {
                if (start) {
                    display.setText(command);
                    start = false;
                } else {
                    display.setText(display.getText() + command);
                }
            }
            // Decimal 
            else if (command.equals(".")) 
            {
                if (start) {
                    display.setText("0.");
                    start = false;
                } 
                else if (!display.getText().contains(".")) 
                {
                    display.setText(display.getText() + ".");
                }
            }
            // Clear
            else if (command.equals("C")) 
            {
                display.setText("0");
                start = true;
                num1 = num2 = result = 0;
            }
            // Clear Entry
            else if (command.equals("CE")) {
                display.setText("0");
                start = true;
            }
            // Backspace
            else if (command.equals("⌫")) 
            {
                String str = display.getText();
                if (str.length() > 1) 
                {
                    display.setText(str.substring(0, str.length() - 1));
                } else {
                    display.setText("0");
                    start = true;
                }
            }
            // Plus and Minus
            else if (command.equals("+/-")) 
            {
                double temp = Double.parseDouble(display.getText());
                temp = temp * (-1);
                display.setText(df.format(temp));
            }
            //  operator
            else if (command.equals("+") || command.equals("-") || 
                     command.equals("×") || command.equals("÷")) {
                if (!start) 
                {
                    if (operator != 0) 
                    {
                        calculate();
                    } 
                    else 
                    {
                        result = Double.parseDouble(display.getText());
                    }
                    display.setText(df.format(result));
                    num1 = result;
                }
                operator = command.charAt(0);
                start = true;
            }
            // operator part2
            else if (command.equals("x²")) 
            {
                double temp = Double.parseDouble(display.getText());
                temp = temp * temp;
                display.setText(df.format(temp));
                start = true;
            }
            else if (command.equals("²√x")) 
            {
                double temp = Double.parseDouble(display.getText());
                if (temp < 0) {
                    display.setText("Error");
                } else {
                    temp = Math.sqrt(temp);
                    display.setText(df.format(temp));
                }
                start = true;
            }
            else if (command.equals("1/x")) 
            {
                double temp = Double.parseDouble(display.getText());
                if (temp == 0) {
                    display.setText("Cannot divide by zero");
                } else 
                {
                    temp = 1 / temp;
                    display.setText(df.format(temp));
                }
                start = true;
            }
            else if (command.equals("%")) 
            {
                if (operator != 0) 
                {
                    num2 = Double.parseDouble(display.getText());
                    // Percentage calculate
                    num2 = num1 * (num2 / 100);
                    display.setText(df.format(num2));
                } 
                else 
                {
                    double temp = Double.parseDouble(display.getText());
                    temp = temp / 100;
                    display.setText(df.format(temp));
                }
                start = true;
            }
            // Equals
            else if (command.equals("=")) 
            {
                if (operator != 0) {
                    calculate();
                    display.setText(df.format(result));
                    num1 = result;
                    operator = 0;
                }
                start = true;
            }
        } catch (Error ex) 
        {
            display.setText("Error");
            start = true;
            operator = 0;
        } catch (ArithmeticException ex) 
        {
            display.setText("Cannot divide by zero");
            start = true;
            operator = 0;
        }
    }

    private void calculate() {
        num2 = Double.parseDouble(display.getText());
        
        switch (operator) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '×':
                result = num1 * num2;
                break;
            case '÷':
                if (num2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = num1 / num2;
                break;
        }
    }

    public static void main(String[] args) 
    {
        new Calculator();
    }
}