package ui;

import db.DBUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;

public class StudentDashboard extends JFrame {
    private String studentUsername;
    private DefaultListModel<String> quizListModel;
    private JList<String> quizList;
    private JButton startQuizBtn, logoutBtn;
    private HashMap<String, Integer> quizMap;

    public StudentDashboard(String studentUsername) {
        this.studentUsername = studentUsername;
        setTitle("Student Dashboard - " + studentUsername);

        // 🔹 Background Panel
        JPanel bgPanel = new JPanel() {
            private Image bg = new ImageIcon(getClass().getResource("Student Dashboard Background.jpg")).getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(null);
        setContentPane(bgPanel);

        // 🔹 Fonts
        Font cambriaFont = new Font("Cambria", Font.PLAIN, 20);
        Font cambriaBold = new Font("Cambria", Font.BOLD, 32);
        Font quizFont = new Font("Cambria", Font.PLAIN, 24); // Larger font for quiz titles

        // 🔹 Title Label
        JLabel titleLabel = new JLabel("Student Dashboard");
        titleLabel.setFont(cambriaBold);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(650, 50, 400, 50);
        bgPanel.add(titleLabel);

        // 🔹 Quiz List
        quizMap = new HashMap<>();
        quizListModel = new DefaultListModel<>();
        quizList = new JList<>(quizListModel);
        quizList.setFont(quizFont);
        quizList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(quizList);
        scrollPane.setBounds(500, 150, 600, 400);
        bgPanel.add(scrollPane);

        // 🔹 Start Quiz Button
        startQuizBtn = new JButton("Start Quiz");
        startQuizBtn.setFont(cambriaFont);
        startQuizBtn.setBackground(new Color(66, 135, 245));
        startQuizBtn.setForeground(Color.WHITE);
        startQuizBtn.setFocusPainted(false);
        startQuizBtn.setBorderPainted(false);
        startQuizBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startQuizBtn.setBounds(600, 600, 220, 50);
        bgPanel.add(startQuizBtn);

        startQuizBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                startQuizBtn.setBackground(new Color(45, 100, 220));
            }
            public void mouseExited(MouseEvent e) {
                startQuizBtn.setBackground(new Color(66, 135, 245));
            }
        });

        // 🔹 Logout Button
        logoutBtn = new JButton("Logout");
        logoutBtn.setFont(cambriaFont);
        logoutBtn.setBackground(new Color(66, 135, 245));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setBounds(850, 600, 220, 50);
        bgPanel.add(logoutBtn);

        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(new Color(45, 100, 220));
            }
            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(new Color(66, 135, 245));
            }
        });

        // 🔹 Button Actions
        startQuizBtn.addActionListener(e -> startQuiz());
        logoutBtn.addActionListener(e -> logout());

        // 🔹 Load Quizzes
        loadQuizzes();

        setSize(1600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    // 🔹 Load Quizzes
    private void loadQuizzes() {
        quizListModel.clear();
        quizMap.clear();
        try (Connection con = DBUtil.getConnection()) {
            String sql = "SELECT id, title FROM quizzes";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                quizListModel.addElement(title);
                quizMap.put(title, id);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading quizzes: " + ex.getMessage());
        }
    }

    // 🔹 Start Quiz
    private void startQuiz() {
        String selectedQuiz = quizList.getSelectedValue();
        if (selectedQuiz == null) {
            JOptionPane.showMessageDialog(this, "Please select a quiz to start.");
            return;
        }
        int quizId = quizMap.get(selectedQuiz);
        new QuizAttemptDialog(this, studentUsername, quizId);
    }

    // 🔹 Logout
    private void logout() {
        dispose();
        new LoginFrame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboard("demoStudent"));
    }
}
