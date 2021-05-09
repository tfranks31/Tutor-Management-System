<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="./_view/login.css">
    <link rel="stylesheet" href="login.css">
</head>
<body>
    <div id="loginBox">
        <form action="${pageContext.servletContext.contextPath}/login" method="post"> 
		
            		<h1>Login</h1>
            		<label for="username" class="label">Username</label></br>
            		<input id="input" type="text" name="username" value="${username}"></br></br></br>
            		<label for="password" class="label">Password</label></br>
            		<input id="input" type="password" name="password" value="${password}"></br></br></br>
            		<input id="submit" type="submit" name="login" value="Login"></br></br>
            
        			<c:if test="${! empty errorMessage1}">
                		<div class="error">${errorMessage1}</div>
            		</c:if>
            
           			<input id="submit" type="submit" name="forgot" value="Forgot Password"></br></br>

        </form>
    </div>
</body>
</html>