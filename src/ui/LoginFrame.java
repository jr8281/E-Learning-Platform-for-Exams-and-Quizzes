package ui;

import db.DBUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton loginBtn, registerBtn;

    public LoginFrame() {
        super("E-Learning Login");

        // Custom panel with background image
        JPanel bgPanel = new JPanel() {
            private Image bg = new ImageIcon(getClass().getResource("Login Page Background.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(new GridLayout(4, 2, 10, 10));
        setContentPane(bgPanel);

        // Font
        Font cambriaFont = new Font("Cambria", Font.PLAIN, 30);

        // Add components
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(cambriaFont);
        bgPanel.add(usernameLabel);
        usernameField = new JTextField();
        usernameField.setFont(cambriaFont);
        bgPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(cambriaFont);
        bgPanel.add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setFont(cambriaFont);
        bgPanel.add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(cambriaFont);
        bgPanel.add(roleLabel);
        roleCombo = new JComboBox<>(new String[]{"Faculty", "Student"});
        roleCombo.setFont(cambriaFont);
        bgPanel.add(roleCombo);

        loginBtn = new JButton("Login");
        registerBtn = new JButton("Register");

        // Style buttons
        JButton[] buttons = {loginBtn, registerBtn};
        for (JButton btn : buttons) {
            btn.setBackground(new Color(66, 135, 245));
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setFont(cambriaFont);

            // Hover effect
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    btn.setBackground(new Color(45, 100, 220));
                }
                public void mouseExited(MouseEvent evt) {
                    btn.setBackground(new Color(66, 135, 245));
                }
            });
        }

        bgPanel.add(loginBtn);
        bgPanel.add(registerBtn);

        // Action listeners
        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> new RegistrationDialog(this));

        setSize(1600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        try (Connection con = DBUtil.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                if(role.equals("Faculty")) new InstructorDashboard(username);
                else new StudentDashboard(username);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
