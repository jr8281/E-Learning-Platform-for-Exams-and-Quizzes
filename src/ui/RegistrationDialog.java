package ui;

import db.DBUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationDialog extends JDialog {
    private JTextField nameField, usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton saveBtn, cancelBtn;

    public RegistrationDialog(JFrame parent) {
        super(parent, "User Registration", true);

        // Background panel
        JPanel bgPanel = new JPanel() {
            private Image bg = new ImageIcon(getClass().getResource("Registration Dialog Background.jpg")).getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(null);
        setContentPane(bgPanel);

        // Fonts
        Font cambriaFont = new Font("Cambria", Font.PLAIN, 22);
        Font cambriaBold = new Font("Cambria", Font.BOLD, 24);

        // Labels
        JLabel nameLabel = new JLabel("Name:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel roleLabel = new JLabel("Role:");

        JLabel[] labels = {nameLabel, usernameLabel, passwordLabel, roleLabel};
        for (JLabel lbl : labels) {
            lbl.setFont(cambriaBold);
            lbl.setForeground(Color.BLACK);
            bgPanel.add(lbl);
        }

        // Fields
        nameField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        roleCombo = new JComboBox<>(new String[]{"Faculty", "Student"});

        nameField.setFont(cambriaFont);
        usernameField.setFont(cambriaFont);
        passwordField.setFont(cambriaFont);
        roleCombo.setFont(cambriaFont);

        // Buttons
        saveBtn = new JButton("Save");
        cancelBtn = new JButton("Cancel");
        JButton[] buttons = {saveBtn, cancelBtn};

        for (JButton btn : buttons) {
            btn.setBackground(new Color(66, 135, 245)); // same as Start button
            btn.setForeground(Color.WHITE);
            btn.setFont(cambriaFont);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    btn.setBackground(new Color(45, 100, 220));
                }
                public void mouseExited(MouseEvent evt) {
                    btn.setBackground(new Color(66, 135, 245));
                }
            });
            bgPanel.add(btn);
        }

        // ---------- Coordinates (x, y, width, height) ----------
        // Labels
        nameLabel.setBounds(550, 250, 150, 40);
        usernameLabel.setBounds(550, 330, 150, 40);
        passwordLabel.setBounds(550, 410, 150, 40);
        roleLabel.setBounds(550, 490, 150, 40);

        // Text fields
        nameField.setBounds(750, 250, 300, 40);
        usernameField.setBounds(750, 330, 300, 40);
        passwordField.setBounds(750, 410, 300, 40);
        roleCombo.setBounds(750, 490, 300, 40);

        // Buttons
        saveBtn.setBounds(700, 600, 150, 50);
        cancelBtn.setBounds(900, 600, 150, 50);

        // Add components
        bgPanel.add(nameField);
        bgPanel.add(usernameField);
        bgPanel.add(passwordField);
        bgPanel.add(roleCombo);

        // Button actions
        saveBtn.addActionListener(e -> saveUser());
        cancelBtn.addActionListener(e -> dispose());

        // Frame setup
        setSize(1600, 800);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void saveUser() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        try (Connection con = DBUtil.getConnection()) {
            String sql = "INSERT INTO users (name, username, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, role);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "User registered successfully!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationDialog(new JFrame()));
    }
}
