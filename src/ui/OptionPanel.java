package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class OptionPanel extends JPanel {

  private CheckersWindow window;
  private JButton restartBtn;
  private JButton creditsBtn;
  private JButton exitBtn;

  public OptionPanel(CheckersWindow window) {
    this.window = window;
    setLayout(new BorderLayout(10, 10)); // Add gaps between components
    setBackground(new Color(156, 93, 82)); // Wood brown color

    // Adjust the layout padding to lift the buttons up
    setBorder(new EmptyBorder(5, 10, 10, 10)); // Top, left, bottom, right padding

    // Exit button on the very left with increased size
    exitBtn = createFancyButton(
        "Exit",
        new Font("Arial", Font.BOLD, 20), // Increased font size
        new Color(128, 0, 0), // Deep red
        BorderFactory.createRaisedBevelBorder());
    exitBtn.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.exit(0); // Exit the application
          }
        });

    // Restart button next to Exit with increased size
    restartBtn = createFancyButton(
        "Restart",
        new Font("Arial", Font.BOLD, 20), // Increased font size
        new Color(0, 100, 0), // Dark green
        BorderFactory.createRaisedBevelBorder());
    restartBtn.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            window.restart();
          }
        });

    // Credits button on the top right with increased size
    creditsBtn = createFancyButton(
        "Credits",
        new Font("Arial", Font.BOLD, 20), // Increased font size
        new Color(0, 128, 128), // Teal color
        BorderFactory.createRaisedBevelBorder());
    creditsBtn.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            window.showCredits();
          }
        });

    // Create a sub-panel for the Exit and Restart buttons to control their size and
    // position
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.LINE_AXIS));

    leftPanel.add(exitBtn);
    leftPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Spacer between buttons with transparent color
    leftPanel.add(restartBtn);
    // leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); //
    // Optional: Bottom padding to lift buttons up
    leftPanel.setOpaque(false); // Make the panel transparent

    // Add the sub-panel to the OptionPanel
    add(leftPanel, BorderLayout.WEST);

    // Create a sub-panel for the Credits button to control its size and position
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.LINE_AXIS));
    rightPanel.add(Box.createHorizontalGlue()); // Pushes the Credits button to the right
    rightPanel.add(creditsBtn);
    // rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); // Bottom
    // padding to lift the button up

    // Add the sub-panel to the OptionPanel
    add(rightPanel, BorderLayout.EAST);
  }

  private JButton createFancyButton(
      String text,
      Font font,
      Color baseColor,
      Border border) {

    JButton button = new JButton(text);
    button.setFont(font);
    button.setForeground(Color.WHITE); // White text to stand out on the darker buttons
    button.setBackground(baseColor); // Base color for the button
    button.setMargin(new Insets(40, 40, 40, 40)); // Top, left, bottom, right padding

    // Compound border with a shadow-like effect to give depth
    Border outerBorder = BorderFactory.createRaisedBevelBorder();
    Border innerBorder = new EmptyBorder(15, 25, 15, 25);
    button.setBorder(new CompoundBorder(outerBorder, innerBorder));

    button.setFocusPainted(false); // Avoid painting the focus indicator
    button.setOpaque(true); // Ensure the button is opaque to show the background
    button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor change on hover

    // Increase the button size by setting a margin
    button.setMargin(new Insets(20, 40, 20, 40)); // Padding around the text

    // Hover effect to change button appearance on mouse-over
    button.addMouseListener(
        new java.awt.event.MouseAdapter() {
          public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(baseColor.brighter().brighter()); // darken the button on hover
          }

          public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setBackground(baseColor); // Revert to the base color when not hovered
          }
        });

    return button;
  }

}
