import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

final public class BatalhaNaval {
    private static final int ROW = 8;
    private static final int COLS = 8;
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;
    private final int NUMBER_OF_SHIPS = 10;

    private static final Color RED = new Color(215, 75, 95);
    private static final Color BLUE = new Color(40, 155, 235);
    private static final Color GREEN = new Color(98, 201, 138);

    private static final char SHIP_LETTER = 'N';
    private static final char WAVE_LETTER = '~';
    private static final String X_EMOJI = "\uD83E\uDD2F";
    private static final String SHIPS_EMOJI = "\uD83D\uDEA2";
    private static final String WAVE_EMOJI = "\uD83C\uDF0A";

    private final JFrame frame = new JFrame("Batalha Naval");
    private final JPanel boardPanel = new JPanel(new GridLayout(ROW, COLS));
    private JLabel remainingAttemptsLabel;

    private final char[][] board = new char[ROW][COLS];
    private final JButton[][] visibleBoard = new JButton[ROW][COLS];

    private int remainingAttempts = 30;
    private int reveledShips = 0;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            var game = new BatalhaNaval();
            game.startGame();
        } catch (UnsupportedLookAndFeelException exc) {
            JOptionPane.showMessageDialog(
                    null,
                    "Não foi possível carregar os temas da aplicação",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void startGame() {
        populateBoard();
        buildWindow();
    }

    private void populateBoard() {
        populateBoardWithWater();
        populateBoardWithShips();
        populateButtonsWithWater();
    }

    private void populateBoardWithWater() {
        for (char[] row : board) {
            Arrays.fill(row, WAVE_LETTER);
        }
    }

    private void populateBoardWithShips() {
        var random = new Random();
        int populated = 0;
        while (populated < NUMBER_OF_SHIPS) {
            int column = random.nextInt(8), row = random.nextInt(8);
            if (board[row][column] == WAVE_LETTER) {
                board[row][column] = SHIP_LETTER;
                populated++;
            }
        }
    }

    private void populateButtonsWithWater() {
        for (JButton[] row : visibleBoard) {
            for (int i = 0; i < row.length; i++) {
                JButton button = new JButton(WAVE_EMOJI);
                //button.setFont(new Font("Arial", Font.PLAIN, 28));
                button.setBackground(BLUE);
                row[i] = button;
            }
        }
    }

    private void buildWindow() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        remainingAttemptsLabel = new JLabel("Tentativas restantes: " + remainingAttempts);
        remainingAttemptsLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(remainingAttemptsLabel, BorderLayout.SOUTH);
        frame.setVisible(true);

        addButtonsToWindow();
    }

    private void play(int row, int column) {
        shootCell(row, column);
        checkIfGameIsOver();
    }

    private void addButtonsToWindow() {
        for (int i = 0; i < visibleBoard.length; i++) {
            for (int j = 0; j < visibleBoard[i].length; j++) {

                int finalI = i;
                int finalJ = j;

                visibleBoard[i][j].addActionListener(e -> play(finalI, finalJ));
                boardPanel.add(visibleBoard[i][j]);
            }
        }
    }

    private void shootCell(int row, int column) {
        if (visibleBoard[row][column].getText().equals(WAVE_EMOJI)) {
            if (board[row][column] == SHIP_LETTER) {
                visibleBoard[row][column].setText(SHIPS_EMOJI);
                visibleBoard[row][column].setBackground(GREEN);
                ++reveledShips;
            } else {
                visibleBoard[row][column].setText(X_EMOJI);
                visibleBoard[row][column].setBackground(RED);
            }
            --remainingAttempts;
            remainingAttemptsLabel.setText("Tentativas restantes: " + remainingAttempts);
        }
    }

    private void checkIfGameIsOver() {
        if (reveledShips == NUMBER_OF_SHIPS) {
            JOptionPane.showMessageDialog(null, "Você Ganhou!");
            System.exit(0);
        } else if (remainingAttempts == 0) {
            JOptionPane.showMessageDialog(null, "Você perdeu!");
            System.exit(1);
        }
    }
}
