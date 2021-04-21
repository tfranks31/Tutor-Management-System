<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
    <head>
        <title>Add a Tutor</title>
        <link rel="stylesheet" href="./_view/addTutor.css">
    </head>
    <body>
    	<div class="super-class background-image"></div>
    
   	 	<div class="super-class content">
        <div id="title">
            <c:choose>
                <c:when test="${edit}">
                    Edit Tutor
                </c:when>
                <c:otherwise>
                    Add a Tutor
                </c:otherwise>
            </c:choose>
        </div>

        <form action="${pageContext.servletContext.contextPath}/addTutor" method="post">
            <table>
                <tr>
                    <td>
                        <label for="firstName" class="label">Name:</label>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${user.isAdmin}">
                                <input type="text" id="firstName" name="firstName" placeholder="First Name" value="${firstName}">
                            </c:when>
                            <c:otherwise>
                                <p name="firstName">${firstName}</p>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${user.isAdmin}">
                                <input type="text" id="lastName" name="lastName" placeholder="Last Name" value="${lastName}">
                            </c:when>
                            <c:otherwise>
                                <p name="lastName">${lastName}</p>
                            </c:otherwise>
                        </c:choose>

                    </td>
                <tr>
                    <td>
                        <label for="username" class="label">Credentials:</label>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${user.isAdmin}">
                                <input type="text" id="username" name="username" placeholder="Username" value="${username}">
                            </c:when>
                            <c:otherwise>
                                <p name="username">${username}</p>
                            </c:otherwise>
                        </c:choose>
                    </td>               
                    <td>
                        <c:choose>
                            <c:when test="${user.isAdmin}">
                                <input type="text" id="password" name="password" placeholder="Password" value="${password}">
                            </c:when>
                            <c:otherwise>
                                <p name="password">${password}</p>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="email" class="label">Email:</label>
                    </td>
                    <td>
                    <c:choose>
                        <c:when test="${user.isAdmin}">
                            <input type="text" id="email" name="email" placeholder="Email" value="${email}">
                        </c:when>
                        <c:otherwise>
                            <p name="email">${email}</p>
                        </c:otherwise>
                    </c:choose>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="studentID" class="label">Student Info:</label>
                    </td>
                    <td>                    
                       <c:choose>
                            <c:when test="${user.isAdmin}">
                                <input type="text" id="studentID" name="studentID" placeholder="Student ID" value="${studentID}">
                            </c:when>
                            <c:otherwise>
                                <p name="studentID">${studentID}</p>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>                   
                        <c:choose>
                            <c:when test="${user.isAdmin}">
                                <input type="text" id="accountNumber" name="accountNumber" placeholder="Account Number" value="${accountNumber}">
                            </c:when>
                            <c:otherwise>
                                <p name="accountNumber">${accountNumber}</p>
                            </c:otherwise>
                        </c:choose>     
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="subject" class="label">Tutor Info:</label>
                    </td>
                    <td>  
                        <c:choose>
                            <c:when test="${user.isAdmin}">
                                <input type="text" id="subject" name="subject" placeholder="Subject" value="${subject}">
                            </c:when>
                            <c:otherwise>
                                <p name="subject">${subject}</p>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${user.isAdmin}">
                                <input type="text" id="payRate" name="payRate" placeholder="Pay Rate" value="${payRate}">
                            </c:when>
                            <c:otherwise>
                                <p name="payRate">${payRate}</p>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
            
            <c:if test="${! empty errorMessage}">
                <div class="error">${errorMessage}</div>
            </c:if>
    
            <div id="buttons">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" name="editTutorInfo" value="Edit">
                    </c:when>
                    <c:otherwise>
                        <input type="submit" name="addTutor" value="Add Tutor">
                    </c:otherwise>
                </c:choose>
            </div>
        </form>
        
        <form action="${pageContext.servletContext.contextPath}/addTutor" method="get">
            <input type="submit" id="backBtn" name="back" value="Back">
        </form>
        
        </br>
        </div>
        
    </body>
</html>