package com.example.blogpost;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@WebServlet("/register")
public class UserRegistrationServlet extends HttpServlet {

    private final String url = "jdbc:mysql://localhost:3306/blog_db";
    private final String username = "root";
    private final String password = "root";

    // Check if a user with the given username already exists in the database
    public boolean isUserExists(String username) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Register a new user in the database
    public boolean registerUser(String username, String password) {
        // Hash the password before storing it in the database (use a proper hashing library)
        String hashedPassword = hashPassword(password);

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if the provided username and password match a user in the database
    public boolean authenticateUser(String username, String password) {
        // Hash the provided password and compare it with the stored hashed password
        String hashedPassword = hashPassword(password);

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    return hashedPassword.equals(storedHashedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Implement the method to hash the password using a secure hashing algorithm (e.g., bcrypt)
    private String hashPassword(String password) {
        // Implement the hashing logic here
        return password;
    }
}
