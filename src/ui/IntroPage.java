package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IntroPage extends JFrame {

    public IntroPage() {
        setTitle("QuizHub");
        setSize(1600, 800); // Match your image size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Load background image from same package
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("Intro Page Background.png"));
        JLabel bg = new JLabel(bgIcon);
        bg.setBounds(0, 0, 1600, 800);
        add(bg);

        // Start button
        JButton startButton = new JButton("Start");
        startButton.setBounds(650, 670, 200, 80);

        // Style the button (color + font)
        startButton.setBackground(new Color(66, 135, 245));
        startButton.setForeground(Color.WHITE);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Set Cambria font, bold, size 24
        Font cambriaFont = new Font("Cambria", Font.BOLD, 26);
        startButton.setFont(cambriaFont);

        // Hover effect
        startButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                startButton.setBackground(new Color(45, 100, 220));
            }
            public void mouseExited(MouseEvent evt) {
                startButton.setBackground(new Color(66, 135, 245));
            }
        });

        bg.add(startButton); // Add button on top of background

        // Open LoginFrame on click
        startButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new IntroPage().setVisible(true));
    }
}
