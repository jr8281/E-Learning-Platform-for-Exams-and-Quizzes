package ui;

import db.DBUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LeaderboardDialog extends JDialog {

    private JTable table;
    private DefaultTableModel model;

    public LeaderboardDialog(JFrame parent, String quizTitle) {
        super(parent, "Leaderboard - " + quizTitle, true);
        setLayout(new BorderLayout(10, 10));

        // 🧭 Find quiz_id based on quiz title
        int quizId = getQuizIdByTitle(quizTitle);
        if (quizId == -1) {
            JOptionPane.showMessageDialog(this, "Quiz not found!");
            dispose();
            return;
        }

        // 🏆 Table setup
        model = new DefaultTableModel(new String[]{"Rank", "Student Username", "Score", "Date Taken"}, 0);
        table = new JTable(model);
        loadLeaderboardData(quizId);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        add(closeBtn, BorderLayout.SOUTH);

        setSize(500, 400);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    // 🔹 Fetch quiz ID from title
    private int getQuizIdByTitle(String quizTitle) {
        try (Connection con = DBUtil.getConnection()) {
            String sql = "SELECT id FROM quizzes WHERE title = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, quizTitle);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching quiz ID: " + ex.getMessage());
        }
        return -1;
    }

    // 🔹 Load leaderboard data from scores table
    private void loadLeaderboardData(int quizId) {
        try (Connection con = DBUtil.getConnection()) {
            String sql = "SELECT student_username, score, date_taken FROM scores WHERE quiz_id = ? ORDER BY score DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();

            int rank = 1;
            while (rs.next()) {
                String username = rs.getString("student_username");
                int score = rs.getInt("score");
                Timestamp dateTaken = rs.getTimestamp("date_taken");

                model.addRow(new Object[]{rank++, username, score, dateTaken});
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No scores available for this quiz yet!");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading leaderboard: " + ex.getMessage());
        }
    }
}
