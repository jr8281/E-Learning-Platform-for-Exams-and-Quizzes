package ui;

import db.DBUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddQuestionsDialog extends JDialog {
    private JTextField questionField, option1Field, option2Field, option3Field, option4Field;
    private JComboBox<String> correctOptionCombo;
    private JButton addBtn, finishBtn;
    private int quizId;
    private int questionsAdded = 0;
    private int maxQuestions = 10;

    public AddQuestionsDialog(JFrame parent, String quizTitle) {
        super(parent, "Add Questions for: " + quizTitle, true);

        // Set up panel with padding
        JPanel panel = new JPanel();
        panel.setLayout(null); // using absolute positioning for flexibility
        panel.setBackground(new Color(245, 245, 245));
        setContentPane(panel);

        Font labelFont = new Font("Cambria", Font.BOLD, 20);
        Font fieldFont = new Font("Cambria", Font.PLAIN, 18);
        Font buttonFont = new Font("Cambria", Font.BOLD, 18);

        // Fetch quiz id
        try (Connection con = DBUtil.getConnection()) {
            String sql = "SELECT id FROM quizzes WHERE title = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, quizTitle);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) quizId = rs.getInt("id");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching quiz id: " + ex.getMessage());
        }

        // Labels and TextFields
        JLabel lblQuestion = new JLabel("Question:");
        lblQuestion.setFont(labelFont);
        lblQuestion.setBounds(50, 30, 200, 30);
        panel.add(lblQuestion);

        questionField = new JTextField();
        questionField.setFont(fieldFont);
        questionField.setBounds(250, 30, 500, 35);
        panel.add(questionField);

        JLabel lblOption1 = new JLabel("Option 1:");
        lblOption1.setFont(labelFont);
        lblOption1.setBounds(50, 80, 200, 30);
        panel.add(lblOption1);

        option1Field = new JTextField();
        option1Field.setFont(fieldFont);
        option1Field.setBounds(250, 80, 500, 35);
        panel.add(option1Field);

        JLabel lblOption2 = new JLabel("Option 2:");
        lblOption2.setFont(labelFont);
        lblOption2.setBounds(50, 130, 200, 30);
        panel.add(lblOption2);

        option2Field = new JTextField();
        option2Field.setFont(fieldFont);
        option2Field.setBounds(250, 130, 500, 35);
        panel.add(option2Field);

        JLabel lblOption3 = new JLabel("Option 3:");
        lblOption3.setFont(labelFont);
        lblOption3.setBounds(50, 180, 200, 30);
        panel.add(lblOption3);

        option3Field = new JTextField();
        option3Field.setFont(fieldFont);
        option3Field.setBounds(250, 180, 500, 35);
        panel.add(option3Field);

        JLabel lblOption4 = new JLabel("Option 4:");
        lblOption4.setFont(labelFont);
        lblOption4.setBounds(50, 230, 200, 30);
        panel.add(lblOption4);

        option4Field = new JTextField();
        option4Field.setFont(fieldFont);
        option4Field.setBounds(250, 230, 500, 35);
        panel.add(option4Field);

        JLabel lblCorrect = new JLabel("Correct Option (1-4):");
        lblCorrect.setFont(labelFont);
        lblCorrect.setBounds(50, 280, 250, 30);
        panel.add(lblCorrect);

        correctOptionCombo = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        correctOptionCombo.setFont(fieldFont);
        correctOptionCombo.setBounds(300, 280, 100, 35);
        panel.add(correctOptionCombo);

        // Buttons
        addBtn = new JButton("Add Question");
        addBtn.setFont(buttonFont);
        addBtn.setBounds(250, 340, 220, 50);
        styleButton(addBtn);
        panel.add(addBtn);

        finishBtn = new JButton("Finish");
        finishBtn.setFont(buttonFont);
        finishBtn.setBounds(480, 340, 220, 50);
        styleButton(finishBtn);
        panel.add(finishBtn);

        // Button Actions
        addBtn.addActionListener(e -> addQuestion());
        finishBtn.addActionListener(e -> dispose());

        setSize(850, 450);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(66, 135, 245));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(45, 100, 220));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(66, 135, 245));
            }
        });
    }

    private void addQuestion() {
        String q = questionField.getText();
        String o1 = option1Field.getText();
        String o2 = option2Field.getText();
        String o3 = option3Field.getText();
        String o4 = option4Field.getText();
        int correct = correctOptionCombo.getSelectedIndex() + 1;

        if (q.isEmpty() || o1.isEmpty() || o2.isEmpty() || o3.isEmpty() || o4.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        if (questionsAdded >= maxQuestions) {
            JOptionPane.showMessageDialog(this, "Maximum questions reached for this quiz!");
            return;
        }

        try (Connection con = DBUtil.getConnection()) {
            String sql = "INSERT INTO questions (quiz_id, question_text, option1, option2, option3, option4, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, quizId);
            ps.setString(2, q);
            ps.setString(3, o1);
            ps.setString(4, o2);
            ps.setString(5, o3);
            ps.setString(6, o4);
            ps.setInt(7, correct);
            ps.executeUpdate();

            questionsAdded++;
            JOptionPane.showMessageDialog(this, "Question added! Total: " + questionsAdded);

            // Clear fields
            questionField.setText(""); option1Field.setText(""); option2Field.setText("");
            option3Field.setText(""); option4Field.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame dummyParent = new JFrame();
            dummyParent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dummyParent.setVisible(true);
            new AddQuestionsDialog(dummyParent, "Sample Quiz");
        });
    }
}
