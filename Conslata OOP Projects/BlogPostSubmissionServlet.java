
package com.example.blogpost;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/submit-post")
public class BlogPostSubmissionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection parameters
    private final String url = "jdbc:mysql://localhost:3306/blog_db";
    private final String dbUsername = "root";
    private final String dbPassword = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if the user is authenticated before processing the form data
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            // User is not authenticated, redirect to login page
            response.sendRedirect("login.jsp?error=You need to log in to create a blog post.");
            return;
        }

        // Retrieve the blog post data from the form
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String author = (String) session.getAttribute("username");

        // Database connection variables
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Establish database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Store the submitted blog post in the database
            String insertQuery = "INSERT INTO blog_posts (title, content, author) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setString(3, author);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                // Blog post successfully submitted, redirect to the homepage
                response.sendRedirect("homepage.jsp?message=Blog post submitted successfully.");
            } else {
                // Blog post submission failed
                response.sendRedirect("create_post.jsp?error=Failed to submit the blog post. Please try again.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("create_post.jsp?error=An error occurred while processing the blog post. Please try again later.");
        } finally {
            // Close resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
