package com.example.blogpost;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/create-post")
public class BlogPostFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            // User is authenticated, display the blog post creation form
            request.getRequestDispatcher("create_post.jsp").forward(request, response);
        } else {
            // User is not authenticated, redirect to login page
            response.sendRedirect("login.jsp?error=You need to log in to create a blog post.");
        }
    }

    @WebServlet("/login")
    public static class UserLoginServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        // Database connection parameters
        private final String url = "jdbc:mysql://localhost:3306/blog_db";
        private final String dbUsername = "root";
        private final String dbPassword = "root";

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            // Retrieve user input from the login form
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Database connection variables
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                // Establish database connection
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, dbUsername, dbPassword);

                // Retrieve hashed password from the database for the provided username
                String selectQuery = "SELECT password FROM users WHERE username = ?";
                stmt = conn.prepareStatement(selectQuery);
                stmt.setString(1, username);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    String hashedPasswordFromDB = rs.getString("password");

                    // Assuming you have a method to hash the provided password for comparison
                    String hashedPassword = hashPassword(password);

                    // Compare the hashed passwords to authenticate the user
                    if (hashedPasswordFromDB.equals(hashedPassword)) {
                        // Create a session and store the username for future requests
                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);

                        // Redirect to the homepage or a dashboard after successful login
                        response.sendRedirect("homepage.jsp");
                        return;
                    }
                }

                // Handle login failure
                response.sendRedirect("login.jsp?error=Invalid username or password. Please try again.");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                response.sendRedirect("login.jsp?error=Login failed. Please try again later.");
            } finally {
                // Close resources
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Implement the method to hash the password using a secure hashing algorithm (e.g., bcrypt)
        private String hashPassword(String password) {
            // Implement the hashing logic here
            return password;
        }
    }
}

