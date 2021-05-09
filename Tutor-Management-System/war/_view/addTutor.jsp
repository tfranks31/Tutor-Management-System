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
                <c:when test="${!user.isAdmin}">
                    Tutor Profile
                </c:when>
                <c:when test="${addTutor}">
                    Add a Tutor
                </c:when>
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
                                <input type="text" id="firstName" name="firstName" value="${firstName}" style="background-color: #d2d2d2; border:2px solid #d2d2d2;" readonly>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${user.isAdmin}">
                                <input type="text" id="lastName" name="lastName" placeholder="Last Name" value="${lastName}">
                            </c:when>
                            <c:otherwise>
                                <input type="text" id="lastName" name="lastName" value="${lastName}" style="background-color: #d2d2d2; border:2px solid #d2d2d2;" readonly>
                            </c:otherwise>
                        </c:choose>

                    </td>
                <tr>
                    <td>
                    	<c:choose>  
		                	<c:when test="${addTutor}">
		                		<label for="email" class="label" >Email:</label>
		                	</c:when>
                            <c:otherwise>
                                <label for="username" class="label">Credentials:</label>
                            </c:otherwise>
                        </c:choose>                    
                    </td>
                    <td>
                        <c:choose>  
		                	<c:when test="${addTutor}">
		                		<input type="text" id="username" name="email" placeholder="Email" value="${email}">
		                	</c:when>
                            <c:otherwise>
                                <input type="text" id="username" name="username" value="${username}" style="background-color: #d2d2d2; border:2px solid #d2d2d2;" readonly>
                            </c:otherwise>
                        </c:choose>
                    </td>               
                    <td>
                        <c:choose>
                            <c:when test="${user.isAdmin}">
                                <input type="password" id="password" name="password" placeholder="Password" value="${password}">
                            </c:when>
                            <c:otherwise>
                                <input type="password" id="password" name="password" value="${password}">
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <c:choose>  
                	<c:when test="${addTutor}">

                	</c:when>
                    <c:otherwise>
                        <tr>
		                    <td>
		                        <label for="email" class="label" >Email:</label>
		                    </td>
		                    <td>
		                    <c:choose>
		                        <c:when test="${user.isAdmin}">
		                            <input type="text" id="email" name="email" placeholder="Email" value="${email}">
		                        </c:when>
		                        <c:otherwise>
		                            <input type="text" id="email" name="email" value="${email}" style="background-color: #d2d2d2; border:2px solid #d2d2d2;" readonly>
		                        </c:otherwise>
		                    </c:choose>
		                    </td>
		                </tr>
                    </c:otherwise>
                </c:choose>
                
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
                                <input type="text" id="studentID" name="studentID" value="${studentID}" style="background-color: #d2d2d2; border:2px solid #d2d2d2;" readonly>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>                   
                        <c:choose>
                            <c:when test="${user.isAdmin}">
                                <input type="text" id="accountNumber" name="accountNumber" placeholder="Account Number" value="${accountNumber}">
                            </c:when>
                            <c:otherwise>
                                <input type="text" id="accountNumber" name="accountNumber" value="${accountNumber}" style="background-color: #d2d2d2; border:2px solid #d2d2d2;" readonly>
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
                                <input type="text" id="subject" name="subject" value="${subject}" style="background-color: #d2d2d2; border:2px solid #d2d2d2;" readonly>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${user.isAdmin}">
                                <input type="text" id="payRate" name="payRate" placeholder="Pay Rate" value="${payRate}">
                            </c:when>
                            <c:otherwise>
                                <input type="text" id="payRate" name="payRate" value="${payRate}" style="background-color: #d2d2d2; border:2px solid #d2d2d2;" readonly>
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
                    <c:when test="${!user.isAdmin}">
                        <input type="submit" name="updatePassword" value="Update Password">
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