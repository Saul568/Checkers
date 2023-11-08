package ui;

import java.awt.*;
import javax.swing.*;

public class CheckersWindow extends JFrame {

  private static final long serialVersionUID = 8782122389400590079L;

  public static final int DEFAULT_WIDTH = 800;
  public static final int DEFAULT_HEIGHT = 800;
  public static final String DEFAULT_TITLE = "Java Checkers";

  private CheckerBoard board;
  private OptionPanel opts;

  public CheckersWindow() {
    this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TITLE);
  }

  public CheckersWindow(int width, int height, String title) {
    super(title);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(width, height);
    setLocationRelativeTo(null); // Centers the window
    getContentPane().setBackground(new Color(156, 93, 82)); // Wood brown

    board = new CheckerBoard(this);
    opts = new OptionPanel(this);

    setLayout(new BorderLayout());
    add(board, BorderLayout.CENTER);
    add(opts, BorderLayout.SOUTH);

    setVisible(true);
  }

  public CheckerBoard getBoard() {
    return board;
  }

  public void restart() {
    board.getGame().restart();
    board.update();
  }

  public void showCredits() {
    String credits =
      "Developed by:\nSaul Lara\nDavid Wilbanks\nTruc-Vy Hoang\nBardan Dhakal";
    JOptionPane.showMessageDialog(
      this,
      credits,
      "Credits",
      JOptionPane.INFORMATION_MESSAGE
    );
  }

  // Main method to run the application
  public static void main(String[] args) {
    new CheckersWindow();
  }
}
