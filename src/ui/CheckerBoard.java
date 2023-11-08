package ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import logic.MoveGenerator; // Ensure this class exists in the 'logic' package
import model.Board;
import model.Game;

public class CheckerBoard extends JButton {

  private static final long serialVersionUID = -6014690893709316364L;
  private static final int PADDING = 16;

  private Game game;
  private CheckersWindow window;
  private Point selected;
  private boolean selectionValid;
  private Color lightTile = Color.RED;
  private Color darkTile = Color.BLACK;
  private boolean isGameOver;

  public CheckerBoard(CheckersWindow window) {
    this(window, new Game());
  }

  public CheckerBoard(CheckersWindow window, Game game) {
    this.game = game != null ? game : new Game();
    this.window = window;
    configureButton();
    setupMouseListener();
  }

  private void configureButton() {
    setBorderPainted(false);
    setFocusPainted(false);
    setContentAreaFilled(true);
    // setBackground(Color.LIGHT_GRAY);
  }

  private void setupMouseListener() {
    addMouseListener(
      new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          handleClick(e.getX(), e.getY());
        }
      }
    );
  }

  public Game getGame() {
    return game;
  }

  public void update() {
    this.isGameOver = game.isGameOver();
    repaint();
  }

  private void handleClick(int x, int y) {
    // The game is over or the current player isn't human
    if (isGameOver) {
      return;
    }

    // Determine what square (if any) was selected
    int boxSize = Math.min(getWidth(), getHeight()) / 8;
    Point clickedPoint = new Point(
      (x - PADDING) / boxSize,
      (y - PADDING) / boxSize
    );

    // If the point is valid, check if it's a move or selection
    if (Board.isValidPoint(clickedPoint)) {
      if (
        selected != null &&
        game.getBoard().get(Board.toIndex(selected)) != Board.EMPTY
      ) {
        // Try to make a move
        if (game.move(selected, clickedPoint)) {
          // Move was successful
          selected = null;
          isGameOver = game.isGameOver();
        } else {
          // Move was not successful, keep selection
          selected = clickedPoint;
        }
      } else {
        // New selection
        selected = clickedPoint;
        selectionValid =
          MoveGenerator
            .getMoves(game.getBoard(), Board.toIndex(selected))
            .size() >
          0;
      }
    }
    repaint();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON
    );
    Game game = this.game.copy();

    // Perform calculations
    final int BOX_PADDING = 4;
    final int W = getWidth(), H = getHeight();
    final int DIM = W < H ? W : H, BOX_SIZE = (DIM - 2 * PADDING) / 8;
    final int OFFSET_X = (W - BOX_SIZE * 8) / 2;
    final int OFFSET_Y = (H - BOX_SIZE * 8) / 2;
    final int CHECKER_SIZE = Math.max(0, BOX_SIZE - 2 * BOX_PADDING);

    // Draw checker board
    g.setColor(Color.BLACK);
    g.drawRect(OFFSET_X - 1, OFFSET_Y - 1, BOX_SIZE * 8 + 1, BOX_SIZE * 8 + 1);
    g.setColor(lightTile);
    g.fillRect(OFFSET_X, OFFSET_Y, BOX_SIZE * 8, BOX_SIZE * 8);
    g.setColor(darkTile);
    for (int y = 0; y < 8; y++) {
      for (int x = (y + 1) % 2; x < 8; x += 2) {
        g.fillRect(
          OFFSET_X + x * BOX_SIZE,
          OFFSET_Y + y * BOX_SIZE,
          BOX_SIZE,
          BOX_SIZE
        );
      }
    }

    // Highlight the selected tile if valid
    if (Board.isValidPoint(selected)) {
      g.setColor(selectionValid ? Color.GREEN : new Color(0, 0, 255));
      g.fillRect(
        OFFSET_X + selected.x * BOX_SIZE,
        OFFSET_Y + selected.y * BOX_SIZE,
        BOX_SIZE,
        BOX_SIZE
      );
    }

    // Draw the checkers
    Board b = game.getBoard();
    for (int y = 0; y < 8; y++) {
      int cy = OFFSET_Y + y * BOX_SIZE + BOX_PADDING;
      for (int x = (y + 1) % 2; x < 8; x += 2) {
        int id = b.get(x, y);

        // Empty, just skip
        if (id == Board.EMPTY) {
          continue;
        }

        int cx = OFFSET_X + x * BOX_SIZE + BOX_PADDING;

        // Black checker
        if (id == Board.BLACK_CHECKER) {
          g.setColor(Color.DARK_GRAY);
          g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.LIGHT_GRAY);
          g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.BLACK);
          g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.LIGHT_GRAY);
          g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
        }
        // Black king
        else if (id == Board.BLACK_KING) {
          g.setColor(Color.DARK_GRAY);
          g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.LIGHT_GRAY);
          g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.DARK_GRAY);
          g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.LIGHT_GRAY);
          g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.BLACK);
          g.fillOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
        }
        // White checker
        else if (id == Board.WHITE_CHECKER) {
          g.setColor(Color.LIGHT_GRAY);
          g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.DARK_GRAY);
          g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.WHITE);
          g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.DARK_GRAY);
          g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
        }
        // White king
        else if (id == Board.WHITE_KING) {
          g.setColor(Color.LIGHT_GRAY);
          g.fillOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.DARK_GRAY);
          g.drawOval(cx + 1, cy + 2, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.LIGHT_GRAY);
          g.fillOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.DARK_GRAY);
          g.drawOval(cx, cy, CHECKER_SIZE, CHECKER_SIZE);
          g.setColor(Color.WHITE);
          g.fillOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
        }

        // Any king (add some extra highlights)
        if (id == Board.BLACK_KING || id == Board.WHITE_KING) {
          g.setColor(new Color(255, 240, 0));
          g.drawOval(cx - 1, cy - 2, CHECKER_SIZE, CHECKER_SIZE);
          g.drawOval(cx + 1, cy, CHECKER_SIZE - 4, CHECKER_SIZE - 4);
        }

        // Set the color for the labels to white
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Variables for label positioning
        int xOffset = OFFSET_X;
        int yOffset = OFFSET_Y;
        int boxSize = (Math.min(getWidth(), getHeight()) - 2 * PADDING) / 8;

        // Draw the labels on the top
        String rows = "12345678";
        for (int i = 0; i < 8; i++) {
          String label = String.valueOf(rows.charAt(i));
          int labelWidth = g.getFontMetrics().stringWidth(label);
          int labelX = xOffset + (i * boxSize) + (boxSize - labelWidth) / 2;
          int labelY = yOffset - g.getFontMetrics().getHeight() / 2; // move up by half the label height
          g.drawString(label, labelX, labelY);
        }

        // Draw the labels on the left side
        String cols = "ABCDEFGH";
        for (int i = 0; i < 8; i++) {
          String label = String.valueOf(cols.charAt(i));
          int labelX =
            xOffset - g.getFontMetrics().stringWidth(label) - PADDING / 2; // move left by label width + half padding
          int labelY =
            yOffset +
            (i * boxSize) +
            (boxSize + g.getFontMetrics().getAscent()) /
            2;
          g.drawString(label, labelX, labelY);
        }
      }
    }

    // Draw the player turn sign
    String msg = game.isP1Turn() ? "Black's turn" : "White's turn";
    Font font = new Font("SansSerif", Font.BOLD, 12); // Choose a more visible font size and style
    g.setFont(font);

    // Calculate the width and height of the text with padding
    int padding = 10; // Padding around the text
    FontMetrics fm = g.getFontMetrics();
    int width = fm.stringWidth(msg) + 2 * padding; // Width with padding
    int height = fm.getHeight() + padding; // Height with padding

    // Define the background and text colors
    Color back = game.isP1Turn() ? Color.BLACK : Color.WHITE;
    Color front = game.isP1Turn() ? Color.WHITE : Color.BLACK;

    // Set the color and draw the background rectangle
    g.setColor(back);
    int rectX = W / 2 - width / 2; // X-coordinate for the rectangle
    int rectY = OFFSET_Y + 8 * BOX_SIZE + 2 - height / 2; // Y-coordinate for the rectangle, centered
    g.fillRect(rectX, rectY, width, height);

    // Draw a border around the sign to make it stand out
    g.setColor(Color.GRAY); // Border color
    g.drawRect(rectX, rectY, width, height);

    // Set the text color and draw the string
    g.setColor(front);
    int textX = W / 2 - width / 2 + padding; // X-coordinate for the text, with padding
    int textY = rectY + fm.getAscent() + (height - fm.getHeight()) / 2; // Y-coordinate for the text, vertically
    // centered with padding
    g.drawString(msg, textX, textY);

    // Draw a game over sign
    if (isGameOver) {
      g.setFont(new Font("Arial", Font.BOLD, 20));
      msg = "Game Over!";
      width = g.getFontMetrics().stringWidth(msg);
      g.setColor(new Color(240, 240, 255));
      g.fillRoundRect(
        W / 2 - width / 2 - 5,
        OFFSET_Y + BOX_SIZE * 4 - 16,
        width + 10,
        30,
        10,
        10
      );
      g.setColor(Color.RED);
      g.drawString(msg, W / 2 - width / 2, OFFSET_Y + BOX_SIZE * 4 + 7);
    }
  }
}
