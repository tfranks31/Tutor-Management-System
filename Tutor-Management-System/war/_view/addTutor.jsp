<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        	<table>
        		<tr>
        			<td>
        				<label for="firstName" class="label">First Name:</label>
        			</td>
        			<td>
        				<input type="text" id="firstName" name="firstName" value="${firstName}">
        			</td>
        		</tr>
        		<tr>
        			<td>
        				<label for="lastName" class="label">Last Name:</label>
        			</td>
        			<td>
        				<input type="text" id="lastName" name="lastName" value="${lastName}">
        			</td>
        		</tr>
        		<tr>
        			<td>
        				<label for="username" class="label">Username:</label>
        			</td>
        			<td>
        				<input type="text" id="username" name="username" value="${username}">
        			</td>
        		</tr>
        		<tr>
        			<td>
        				<label for="password" class="label">Password:</label>
        			</td>
        			<td>
        				<input type="text" id="password" name="password" value="${password}">
        			</td>
        		</tr>
        		<tr>
        			<td>
        				<label for="email" class="label">Email:</label>
        			</td>
        			<td>
        				<input type="text" id="email" name="email" value="${email}">
        			</td>
        		</tr>
        		<tr>
        			<td>
        				<label for="accountNumber" class="label">Account Number:</label>
        			</td>
        			<td>
        				<input type="text" id="accountNumber" name="accountNumber" value="${accountNumber}">
        			</td>
        		</tr>
        		<tr>
        			<td>
        				<label for="subject" class="label">Subject:</label>
        			</td>
        			<td>
        				<input type="text" id="subject" name="subject" value="${subject}">
        			</td>
        		</tr>
        		<tr>
        			<td>
        				<label for="payRate" class="label">Pay Rate:</label>
        			</td>
        			<td>
        				<input type="text" id="payRate" name="payRate" value="${payRate}">
        			</td>
        		</tr>
        	</table>
	        
	        <c:if test="${! empty errorMessage}">
				<div class="error">${errorMessage}</div>
			</c:if>
	
	        <div id="buttons">
	            <input type="submit" name="addTutor" value="Add Tutor">
	        </div>
        </form>
        
        <form action="${pageContext.servletContext.contextPath}/addTutor" method="get">
        	<input type="submit" name="back" value="Back">
        </form>
    </body>
</html>