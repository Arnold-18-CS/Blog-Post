public class BlogPost {
    private String title;
    private String content;
    private String author;

    public BlogPost(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    // Getters and setters (You can add more methods as needed)

    @Override
    public String toString() {
        return "Title: " + title + ", Content: " + content + ", Author: " + author;
    }
}

