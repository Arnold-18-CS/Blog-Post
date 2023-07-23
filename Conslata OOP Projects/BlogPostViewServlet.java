package com.example.blogpost;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/view-posts")
public class BlogPostViewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection parameters
    private final String url = "jdbc:mysql://localhost:3306/blog_db";
    private final String dbUsername = "root";
    private final String dbPassword = "root";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if the user is authenticated before displaying the blog posts
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            List<BlogPost> blogPosts = fetchAllBlogPosts();
            request.setAttribute("blogPosts", blogPosts);
            request.getRequestDispatcher("homepage.jsp").forward(request, response);
        } else {
            // User is not authenticated, redirect to login page
            response.sendRedirect("login.jsp?error=You need to log in to view blog posts.");
        }
    }

    // Method to fetch all blog posts from the database
    private List<BlogPost> fetchAllBlogPosts() {
        List<BlogPost> blogPosts = new ArrayList<>();

        // Database connection variables
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establish database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Retrieve all blog posts from the database
            String selectQuery = "SELECT * FROM blog_posts";
            stmt = conn.prepareStatement(selectQuery);
            rs = stmt.executeQuery();

            // Iterate through the result set and create BlogPost objects
            while (rs.next()) {
                int postId = rs.getInt("post_id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String author = rs.getString("author");
                BlogPost blogPost = new BlogPost(postId, title, content, author);
                blogPosts.add(blogPost);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
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

        return blogPosts;
    }
}
