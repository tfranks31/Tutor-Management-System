<!DOCTYPE html>
<html>
    <head>
        <title>Add a Tutor</title>
        <link rel="stylesheet" href="./_view/addTutor.css">
    </head>
    <body>
        <div id="title">
            Add a Tutor
        </div>
        <form action="${pageContext.servletContext.contextPath}/addTutor" method="post">
        	<div id="labels">
	            <label for="name" class="label">Name:</label>
	            <br>
	            <label for="username" class="label">Username:</label>
	            <br>
	            <label for="password" class="label">Password:</label>
	            <br>
	            <label for="email" class="label">Email:</label>
	            <br>
	            <label for="accountNumber" class="label">Account Number:</label>
	            <br>
	            <label for="subject" class="label">Subject:</label>
	            <br>
	            <label for="payRate" class="label">Pay Rate:</label>
	            <br>
	        </div>
	
	        <div id="textFields">
	            <input type="text" id="name" name="name" value="${name}">
	            <br>
	            <input type="text" id="username" name="username" value="${username}">
	            <br>
	            <input type="text" id="password" name="password" value="${password}">
	            <br>
	            <input type="text" id="email" name="email" value="${email}">
	            <br>
	            <input type="text" id="accountNumber" name="accountNumber" value="${accountNumber}">
	            <br>
	            <input type="text" id="subject" name="subject" value="${subject}">
	            <br>
	            <input type="text" id="payRate" name="payRate" value="${payRate}">
	            <br>
	        </div>
	
	        <div id="buttons">
	            <input type="submit" name="addTutor" value="Add Tutor">
	        </div>
        </form>
        
        <form action="${pageContext.servletContext.contextPath}/addTutor" method="get">
        	<input type="submit" name="back" value="Back">
        </form>
    </body>
</html>