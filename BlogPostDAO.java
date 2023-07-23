import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlogPostDAO {
    private Connection connection;

    public BlogPostDAO() {
        // Get the singleton instance of DBConnection for database access
        DBConnection dbConnection = DBConnection.getInstance();
        connection = dbConnection.getConnection();
    }

    // Get all blog posts from the database
    public List<BlogPost> getAllBlogPosts() {
        List<BlogPost> blogPosts = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM blog_posts";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String author = resultSet.getString("author");
                BlogPost blogPost = new BlogPost(title, content, author);
                blogPosts.add(blogPost);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return blogPosts;
    }
}
