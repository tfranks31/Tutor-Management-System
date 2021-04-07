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
                        <label for="firstName" class="label">Name:</label>
                    </td>
                    <td>
                        <input type="text" id="firstName" name="firstName" placeholder="First Name" value="${firstName}">
                    </td>
                    <td>
                        <input type="text" id="lastName" name="lastName" placeholder="Last Name" value="${lastName}">
                    </td>
                <tr>
                    <td>
                        <label for="username" class="label">Credentials:</label>
                    </td>
                    <td>
                        <input type="text" id="username" name="username" placeholder="Username" value="${username}">
                    </td>               
                    <td>
                        <input type="text" id="password" name="password" placeholder="Password" value="${password}">
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="email" class="label">Email:</label>
                    </td>
                    <td>
                        <input type="text" id="email" name="email" placeholder="Email" value="${email}">
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="studentID" class="label">Student Info:</label>
                    </td>
                    <td>
                        <input type="text" id="studentID" name="studentID" placeholder="Student ID" value="${studentID}">
                    </td>
                    <td>
                        <input type="text" id="accountNumber" name="accountNumber" placeholder="Account Number" value="${accountNumber}">
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="subject" class="label">Tutor Info:</label>
                    </td>
                    <td>
                        <input type="text" id="subject" name="subject" placeholder="Subject" value="${subject}">
                    </td>
                    <td>
                        <input type="text" id="payRate" name="payRate" placeholder="Pay Rate" value="${payRate}">
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
            <input type="submit" id="backBtn" name="back" value="Back">
        </form>
        
        </br>
        
    </body>
</html>