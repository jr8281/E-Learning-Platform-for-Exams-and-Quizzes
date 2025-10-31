package ui;

import db.DBUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class InstructorDashboard extends JFrame {
    private String facultyUsername;
    private DefaultListModel<String> quizListModel;
    private JList<String> quizList;
    private JButton addQuizBtn, addQuestionBtn, viewLeaderboardBtn, logoutBtn;

    public InstructorDashboard(String facultyUsername) {
        super("Faculty Dashboard - " + facultyUsername);
        this.facultyUsername = facultyUsername;

        // 🔹 Background Panel
        JPanel bgPanel = new JPanel() {
            private Image bg = new ImageIcon(getClass().getResource("Login Page Background.jpg")).getImage();
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

        // 🔹 Title Label
        JLabel titleLabel = new JLabel("Instructor Dashboard");
        titleLabel.setFont(cambriaBold);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(650, 50, 400, 50);
        bgPanel.add(titleLabel);

        // 🔹 Quiz List
        quizListModel = new DefaultListModel<>();
        quizList = new JList<>(quizListModel);
        quizList.setFont(cambriaFont);
        quizList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(quizList);
        scrollPane.setBounds(500, 150, 600, 400);
        bgPanel.add(scrollPane);

        // 🔹 Add Quiz Button
        addQuizBtn = new JButton("Add Quiz");
        addQuizBtn.setFont(cambriaFont);
        addQuizBtn.setBackground(new Color(66, 135, 245));
        addQuizBtn.setForeground(Color.WHITE);
        addQuizBtn.setFocusPainted(false);
        addQuizBtn.setBorderPainted(false);
        addQuizBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addQuizBtn.setBounds(300, 600, 220, 50);
        bgPanel.add(addQuizBtn);

        addQuizBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                addQuizBtn.setBackground(new Color(45, 100, 220));
            }
            public void mouseExited(MouseEvent e) {
                addQuizBtn.setBackground(new Color(66, 135, 245));
            }
        });

        // 🔹 Add Question Button
        addQuestionBtn = new JButton("Add Questions");
        addQuestionBtn.setFont(cambriaFont);
        addQuestionBtn.setBackground(new Color(66, 135, 245));
        addQuestionBtn.setForeground(Color.WHITE);
        addQuestionBtn.setFocusPainted(false);
        addQuestionBtn.setBorderPainted(false);
        addQuestionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addQuestionBtn.setBounds(540, 600, 220, 50);
        bgPanel.add(addQuestionBtn);

        addQuestionBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                addQuestionBtn.setBackground(new Color(45, 100, 220));
            }
            public void mouseExited(MouseEvent e) {
                addQuestionBtn.setBackground(new Color(66, 135, 245));
            }
        });

        // 🔹 View Leaderboard Button
        viewLeaderboardBtn = new JButton("View Leaderboard");
        viewLeaderboardBtn.setFont(cambriaFont);
        viewLeaderboardBtn.setBackground(new Color(66, 135, 245));
        viewLeaderboardBtn.setForeground(Color.WHITE);
        viewLeaderboardBtn.setFocusPainted(false);
        viewLeaderboardBtn.setBorderPainted(false);
        viewLeaderboardBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewLeaderboardBtn.setBounds(780, 600, 220, 50);
        bgPanel.add(viewLeaderboardBtn);

        viewLeaderboardBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                viewLeaderboardBtn.setBackground(new Color(45, 100, 220));
            }
            public void mouseExited(MouseEvent e) {
                viewLeaderboardBtn.setBackground(new Color(66, 135, 245));
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
        logoutBtn.setBounds(1020, 600, 220, 50);
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
        addQuizBtn.addActionListener(e -> addQuiz());
        addQuestionBtn.addActionListener(e -> {
            String selectedQuiz = quizList.getSelectedValue();
            if (selectedQuiz == null) {
                JOptionPane.showMessageDialog(this, "Please select a quiz first!");
                return;
            }
            new AddQuestionsDialog(this, selectedQuiz);
        });

        viewLeaderboardBtn.addActionListener(e -> {
            String selectedQuiz = quizList.getSelectedValue();
            if (selectedQuiz == null) {
                JOptionPane.showMessageDialog(this, "Please select a quiz to view leaderboard!");
                return;
            }
            new LeaderboardDialog(this, selectedQuiz);
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        // 🔹 Load quizzes
        loadQuizzes();

        setSize(1600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadQuizzes() {
        quizListModel.clear();
        try (Connection con = DBUtil.getConnection()) {
            String sql = "SELECT title FROM quizzes WHERE faculty_username = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, facultyUsername);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizListModel.addElement(rs.getString("title"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading quizzes: " + ex.getMessage());
        }
    }

    private void addQuiz() {
        String title = JOptionPane.showInputDialog(this, "Enter quiz title:");
        if (title == null || title.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Quiz title cannot be empty!");
            return;
        }

        try (Connection con = DBUtil.getConnection()) {
            String sql = "INSERT INTO quizzes (title, faculty_username) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, title.trim());
            ps.setString(2, facultyUsername);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Quiz added successfully!");
            loadQuizzes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding quiz: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new InstructorDashboard("faculty1");
    }
}
