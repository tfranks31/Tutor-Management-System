<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="./_view/forgotPassword.css">
</head>
<body>

    <div id="loginBox">
      <form action="${pageContext.servletContext.contextPath}/login" method="post"> 
           			<label for="username" class="label">Username</label></br>
            		<input id="input" type="text" name="username" value="${username}"></br></br></br>
            		
            		<label for="studentID" class="label">Student ID</label></br>
            		<input id="input" type="text" name="studentID" value="${studentID}"></br></br></br>
            		
            		<label for="password1" class="label">Password</label></br>
            		<input id="input" type="password" name="password1" value="${password1}"></br></br></br>
            		
            		<label for="password2" class="label">Re-enter Password</label></br>
            		<input id="input" type="password" name="password2" value="${password2}"></br></br></br>
            		<input id="submit" type="submit" name="resetPassword" value="Reset Password"></br></br>
            		
            		<input id="submit" type="submit" name="return" value="Return To Login"></br></br>
            
        			<c:if test="${! empty errorMessage2}">
                		<div class="error">${errorMessage2}</div>
            		</c:if>
           </form>
    </div>
</body>
</html>