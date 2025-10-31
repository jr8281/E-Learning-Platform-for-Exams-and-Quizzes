package ui;

import db.DBUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class QuizAttemptDialog extends JDialog {
    private int quizId;
    private String studentUsername;
    private ArrayList<Question> questions;
    private int currentQuestion = 0;
    private int score = 0;

    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextBtn, submitBtn;

    private static class Question {
        String text;
        String[] opts;
        int correct;
        Question(String t, String[] o, int c) {
            text = t; opts = o; correct = c;
        }
    }

    public QuizAttemptDialog(JFrame parent, String studentUsername, int quizId) {
        super(parent, "Attempt Quiz", true);
        this.studentUsername = studentUsername;
        this.quizId = quizId;
        questions = new ArrayList<>();

        // Load questions from DB
        try (Connection con = DBUtil.getConnection()) {
            String sql = "SELECT * FROM questions WHERE quiz_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                questions.add(new Question(
                        rs.getString("question_text"),
                        new String[]{
                                rs.getString("option1"),
                                rs.getString("option2"),
                                rs.getString("option3"),
                                rs.getString("option4")
                        },
                        rs.getInt("correct_option")
                ));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading questions: " + ex.getMessage());
            return;
        }

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions found!");
            dispose();
            return;
        }

        // 🔹 Background panel
        JPanel bgPanel = new JPanel() {
            private Image bg = new ImageIcon(getClass().getResource("Quiz Attempt Dialog Background.jpg")).getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(null);
        setContentPane(bgPanel);

        Font cambriaFont = new Font("Cambria", Font.PLAIN, 18);

        // 🔹 Question label
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Cambria", Font.BOLD, 22));
        questionLabel.setForeground(Color.BLACK);
        questionLabel.setBounds(50, 20, 800, 50);
        bgPanel.add(questionLabel);

        // 🔹 Options
        group = new ButtonGroup();
        options = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(cambriaFont);
            options[i].setBounds(50, 100 + i*50, 700, 40);
            options[i].setOpaque(false);
            group.add(options[i]);
            bgPanel.add(options[i]);
        }

        // 🔹 Buttons
        nextBtn = new JButton("Next");
        submitBtn = new JButton("Submit");

        JButton[] buttons = {nextBtn, submitBtn};
        int x = 50, y = 320, width = 150, height = 50, gap = 180;

        for (int i = 0; i < buttons.length; i++) {
            JButton btn = buttons[i];
            btn.setFont(cambriaFont);
            btn.setBackground(new Color(66, 135, 245));
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBounds(x + (i * gap), y, width, height);

            // Hover effect
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(45, 100, 220)); }
                public void mouseExited(MouseEvent e) { btn.setBackground(new Color(66, 135, 245)); }
            });

            bgPanel.add(btn);
        }

        nextBtn.addActionListener(e -> nextQuestion());
        submitBtn.addActionListener(e -> submitQuiz());

        loadQuestion(0);

        setSize(900, 450);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void loadQuestion(int index) {
        group.clearSelection();
        Question q = questions.get(index);
        questionLabel.setText("Q" + (index + 1) + ": " + q.text);
        for (int i = 0; i < 4; i++) options[i].setText(q.opts[i]);
        nextBtn.setEnabled(index < questions.size() - 1);
    }

    private void nextQuestion() { checkAnswer(); if(currentQuestion < questions.size()-1) { currentQuestion++; loadQuestion(currentQuestion);} else nextBtn.setEnabled(false);}
    private void checkAnswer() { Question q = questions.get(currentQuestion); for(int i=0;i<4;i++){ if(options[i].isSelected() && (i+1)==q.correct) score++; } }
    private void submitQuiz() {
        checkAnswer();
        try (Connection con = DBUtil.getConnection()) {
            String sql = "INSERT INTO scores (student_username, quiz_id, score) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, studentUsername);
            ps.setInt(2, quizId);
            ps.setInt(3, score);
            ps.executeUpdate();
        } catch(SQLException ex) { JOptionPane.showMessageDialog(this, "Error saving score: " + ex.getMessage()); }
        JOptionPane.showMessageDialog(this, "Quiz Submitted!\nYour score: " + score + " / " + questions.size());
        dispose();
    }
}
