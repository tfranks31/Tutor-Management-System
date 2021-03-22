<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="./_view/login.css">
</head>
<body>
    <div id="loginBox">
        <form action="${pageContext.servletContext.contextPath}/login" method="post"> 
            <h1>Login</h1>
            <label for="username" class="label">Username</label></br>
            <input id="input" type="text" name="username" value="${username}"></br></br></br>
            <label for="password" class="label">Password</label></br>
            <input id="input" type="password" name="password" value="${password}"></br></br></br>
            <input id="submit" type="submit" name="login" value="Login"></br></br></br>
            <a href="ForgotPassword">Forgot Password</a>
        </form>
    </div>
</body>
</html>