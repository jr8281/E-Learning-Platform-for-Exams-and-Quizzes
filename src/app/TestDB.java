package app;

import db.DBUtil;
import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        Connection con = DBUtil.getConnection();
        if (con != null) {
            System.out.println("Connected to MySQL successfully!");
        } else {
            System.out.println("Connection failed!");
        }
    }
}

