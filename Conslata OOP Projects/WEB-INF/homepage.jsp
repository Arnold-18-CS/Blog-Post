<!DOCTYPE html>
<html>
<head>
    <title>Blog Home Page</title>
</head>
<body>
<h1>Welcome to the Blog</h1>

<%-- Check if there are any blog posts to display --%>
<c:if test="${not empty blogPosts}">
    <ul>
            <%-- Loop through each blog post and display its details --%>
        <c:forEach var="post" items="${blogPosts}">
            <li>
                <h2>${post.title}</h2>
                <p>${post.content}</p>
                <p>Author: ${post.author}</p>
            </li>
        </c:forEach>
    </ul>
</c:if>

<%-- Display a message if there are no blog posts --%>
<c:if test="${empty blogPosts}">
    <p>No blog posts available.</p>
</c:if>

<%-- Link to create a new blog post if the user is authenticated --%>
<c:if test="${not empty sessionScope.username}">
    <p><a href="create-post">Create a New Blog Post</a></p>
</c:if>

<%-- Link to log out if the user is authenticated --%>
<c:if test="${not empty sessionScope.username}">
    <p><a href="logout">Log Out</a></p>
</c:if>
</body>
</html>
