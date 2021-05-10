<!DOCTYPE html>
<html>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Tutor View</title>
            <link rel="stylesheet" href="./_view/search.css">
        </head>

        <body>

                <div class="super-class background-image"></div>

                <div class="super-class content">

                    <h1 class="Header">View Tutors</h1>

                    <form action="${pageContext.servletContext.contextPath}/search" method="get">
                        <input type="submit" class="logout" id="logout" name="tutorPageBack" value="Back">
                    </form>

                    <div class="search">
                        <form action="${pageContext.servletContext.contextPath}/search" method="post">
                            <input type="text" id="searchBox" name="tutorSearch" value="${searchParameter}">
                            <button id="searchBTN">&#128269</button>
                        </form>
                    </div>

                    <c:if test="${! empty tutorName}">
                        <div class="successAdd">${tutorName} was added as a tutor</div>
                    </c:if>

                    <c:if test="${! empty editTutorName}">
                        <div class="successAdd">${editTutorName} was sucessfully updated</div>
                    </c:if>

                    <c:if test="${! empty voucherAdded}">
                        <div class="successAdd">${voucherAdded}</div>
                    </c:if>

                    <div class="table">
                        
                        <table id="searchResults">
                            <th>Profile</th>
                            <th>Tutor Name</th>
                            <th>Subject</th>
                            <th>User Name</th>
                            <th>Student ID</th>
                            </tr>
                       
                            <c:forEach items="${UserTutors}" var="user">
                                <tr>
                                    <td>
                                        <form action="${pageContext.servletContext.contextPath}/search" method="post">
                                            <input type="submit" id="view" name="viewTutorProfile" value="View"></input>
                                            <input type="hidden" name="userTutorID" value="${user.left.accountID}" />
                                        </form>
                                    </td>
                                    <td> ${user.right.name} </td>
                                    <td> ${user.right.subject}</td>
                                    <td> ${user.left.username}</td>
                                    <td> ${user.right.studentID}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>

                    <c:if test="${! empty errorMessage}">
                        <div class="error">${errorMessage}</div>
                    </c:if>

                    <form action="${pageContext.servletContext.contextPath}/search" method="post">
                        <div class="pageNum">
                            <input type="submit" id="previousPageBtn" name="page3" value="&#8592">
                            Page ${pageNumberTutor}
                            <input type="submit" id="nextPageBtn" name="page4" value="&#8594">
                        </div>
                    </form>

                    <c:if test="${user.isAdmin}">
                        <div id="assignVoucher">
                            
                            <form action="${pageContext.servletContext.contextPath}/search" method="post">
                                <input type="submit" id="assignVoucherBtn" name="assignVoucher"
                                    value="Assign Pay Voucher"></input></br></br></br></br>
                                
                                <label for="startDate" class="date1" id="assignVoucherDate">Start Date:</label>
                                <input type="text" class="date1" id="assignVoucherText" name="startDate"
                                    placeholder="MM/DD/YYYY" value="${startDate}"></input>
                                
                                <label for="startDate" class="date2" id="assignVoucherDate">Due Date:</label>
                                <input type="text" class="date2" id="assignVoucherText" name="dueDate"
                                    placeholder="MM/DD/YYYY" value="${dueDate}"></input>
                                </br>
                                
                                <c:if test="${! empty assignErrorMessage}">
                                    <div class="error">${assignErrorMessage}</div>
                                </c:if>
                                
                                <div id="radio">
                                    <input type="radio" id="assign-all" name="assign" value="allTutors" checked>
                                    <label for="assign-all" id="radio-label">All Tutors</label>
                                    <input type="radio" id="assign-one" name="assign" value="oneTutor">
                                    <label for="assign-one" id="radio-label">Specific Tutor:</label>
                                    <input type="text" id="edit-tutor-name" name="assignUserName" placeholder="User Name" value="${assignUserName}"></input>
                                </div>

                            </form>
                        </div>
                    </c:if>

                    <c:choose>
                        <c:when test="${user.isAdmin}">
                            <form action="${pageContext.servletContext.contextPath}/search" method="get">
                                <input type="submit" id="addTutor" name="addTutor" value="Add Tutor"
                                    class="addTutor"></input>
                                </br></br>
                            </form>
                        </c:when>
                        
                        <c:otherwise>
                    
                            <form action="${pageContext.servletContext.contextPath}/search" method="post">
                    
                                <input type="submit" id="addTutor" name="tutorProfile" value="Tutor Profile"
                                    class="addTutor"></input>
                    
                                </br></br>
                            </form>
                            
                        </c:otherwise>

                    </c:choose>
                </br></br></br></br>
                </div>

        </body>

</html>