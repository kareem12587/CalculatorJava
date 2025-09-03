import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame {
    private JButton[][] buttons;
    private boolean xTurn;
    private JLabel statusLabel;
    private JButton resetButton;
    private JPanel gamePanel;
    private JPanel topPanel;
    private int xScore, oScore;
    private JLabel scoreLabel;
    
    // Colors
    private final Color BACKGROUND_COLOR = new Color(44, 62, 80);
    private final Color BUTTON_COLOR = new Color(52, 73, 94);
    private final Color BUTTON_HOVER = new Color(69, 90, 120);
    private final Color X_COLOR = new Color(231, 76, 60);
    private final Color O_COLOR = new Color(52, 152, 219);
    private final Color WIN_COLOR = new Color(46, 204, 113);
    private final Color TEXT_COLOR = Color.WHITE;

    public TicTacToe() {
        initializeGame();
        setupUI();
        setVisible(true);
    }

    private void initializeGame() {
        buttons = new JButton[3][3];
        xTurn = true;
        xScore = 0;
        oScore = 0;
    }

    private void setupUI() {
        setTitle("TIC TAC TOE Game");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // Top panel with score and status
        createTopPanel();
        
        // Game board panel
        createGamePanel();
        
        // Bottom panel with reset button
        createBottomPanel();
    }

    private void createTopPanel() {
        topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(BACKGROUND_COLOR);

        // Score label
        scoreLabel = new JLabel(" X: " + xScore + " |  O: " + oScore, SwingConstants.CENTER);
        scoreLabel.setForeground(TEXT_COLOR);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Status label
        statusLabel = new JLabel(" Player X", SwingConstants.CENTER);
        statusLabel.setForeground(X_COLOR);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        topPanel.add(scoreLabel);
        topPanel.add(statusLabel);
        add(topPanel, BorderLayout.NORTH);
    }

    private void createGamePanel() {
        gamePanel = new JPanel(new GridLayout(3, 3, 8, 8));
        gamePanel.setBackground(BACKGROUND_COLOR);
        gamePanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 10, 20));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = createGameButton(i, j);
                gamePanel.add(buttons[i][j]);
            }
        }

        add(gamePanel, BorderLayout.CENTER);
    }

    private JButton createGameButton(int row, int col) {
        JButton button = new JButton();
        button.setFont(new Font("Arial", Font.BOLD, 60));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.getText().isEmpty()) {
                    button.setBackground(BUTTON_HOVER);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.getText().isEmpty()) {
                    button.setBackground(BUTTON_COLOR);
                }
            }
        });

        button.addActionListener(new ButtonClickListener(row, col));
        return button;
    }

    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        resetButton = new JButton("new game");
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setBackground(new Color(155, 89, 182));
        resetButton.setForeground(Color.BLACK);
        resetButton.setFocusPainted(false);
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.setPreferredSize(new Dimension(150, 40));

        resetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                resetButton.setBackground(new Color(142, 68, 173));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                resetButton.setBackground(new Color(155, 89, 182));
            }
        });

        resetButton.addActionListener(e -> resetGame());

        bottomPanel.add(resetButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private class ButtonClickListener implements ActionListener {
         int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!buttons[row][col].getText().isEmpty()) {
                return;
            }

            // Set X or O
            if (xTurn) {
                buttons[row][col].setText("X");
                buttons[row][col].setForeground(X_COLOR);
            } else {
                buttons[row][col].setText("O");
                buttons[row][col].setForeground(O_COLOR);
            }

            buttons[row][col].setBackground(BUTTON_COLOR);
            

            // Check winner
            if (checkWinner()) {
                String winner = xTurn ? "X" : "O";
                highlightWinningButtons();
                updateScore(winner);
                statusLabel.setText("Player " + winner + " win");
                statusLabel.setForeground(WIN_COLOR);
                disableAllButtons();
                return;
            }

            // Check for tie
            if (isBoardFull()) {
                statusLabel.setText("Draw");
                statusLabel.setForeground(Color.YELLOW);
                return;
            }

            // Switch turns
            xTurn = !xTurn;
            if (xTurn) {
                statusLabel.setText("Player X");
                statusLabel.setForeground(X_COLOR);
            } else {
                statusLabel.setText("Player O");
                statusLabel.setForeground(O_COLOR);
            }
        }
    }


    private boolean checkWinner() {
        String[][] board = new String[3][3];
        
        // Fill board array
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText();
            }
        }

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].isEmpty() && 
                board[i][0].equals(board[i][1]) && 
                board[i][1].equals(board[i][2])) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (!board[0][j].isEmpty() && 
                board[0][j].equals(board[1][j]) && 
                board[1][j].equals(board[2][j])) {
                return true;
            }
        }

        // Check diagonals
        if (!board[0][0].isEmpty() && 
            board[0][0].equals(board[1][1]) && 
            board[1][1].equals(board[2][2])) {
            return true;
        }

        if (!board[0][2].isEmpty() && 
            board[0][2].equals(board[1][1]) && 
            board[1][1].equals(board[2][0])) {
            return true;
        }

        return false;
    }

    private void highlightWinningButtons() {
        String[][] board = new String[3][3];
        
        // Fill board array
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText();
            }
        }

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].isEmpty() && 
                board[i][0].equals(board[i][1]) && 
                board[i][1].equals(board[i][2])) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setBackground(WIN_COLOR);
                }
                return;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (!board[0][j].isEmpty() && 
                board[0][j].equals(board[1][j]) && 
                board[1][j].equals(board[2][j])) {
                for (int i = 0; i < 3; i++) {
                    buttons[i][j].setBackground(WIN_COLOR);
                }
                return;
            }
        }

        // Check diagonals
        if (!board[0][0].isEmpty() && 
            board[0][0].equals(board[1][1]) && 
            board[1][1].equals(board[2][2])) {
            buttons[0][0].setBackground(WIN_COLOR);
            buttons[1][1].setBackground(WIN_COLOR);
            buttons[2][2].setBackground(WIN_COLOR);
            return;
        }

        if (!board[0][2].isEmpty() && 
            board[0][2].equals(board[1][1]) && 
            board[1][1].equals(board[2][0])) {
            buttons[0][2].setBackground(WIN_COLOR);
            buttons[1][1].setBackground(WIN_COLOR);
            buttons[2][0].setBackground(WIN_COLOR);
        }
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateScore(String winner) {
        if (winner.equals("X")) {
            xScore++;
        } else {
            oScore++;
        }
        scoreLabel.setText("ْX: " + xScore + " O: " + oScore);
    }

    private void disableAllButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(BUTTON_COLOR);
                buttons[i][j].setEnabled(true);
            }
        }
        
        xTurn = true;
        statusLabel.setText("Player X");
        statusLabel.setForeground(X_COLOR);
    }

    public static void main(String[] args) 
    {
        new TicTacToe();
    }
}