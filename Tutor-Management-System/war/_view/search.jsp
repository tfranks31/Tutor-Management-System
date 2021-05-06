<!DOCTYPE html>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search View</title>
    <link rel="stylesheet" href="./_view/search.css">
</head>

<body>

	<%
	
	response.setHeader("cache-control", "no-cache, no-store, must-revalidate");
	
	response.setHeader("pragma", "no-cache");
	
	response.setHeader("expires", "0");
	
	if (session.getAttribute("user") == null) {
		response.sendRedirect("login.jsp");
	}
	
	%>
	
	<div class="super-class background-image"></div>
	
	<div class="super-class content">
	
    	<h1 class = "Header"> Search</h1>
    	
    	<form action="${pageContext.servletContext.contextPath}/search" method="get">
        	<input type="submit" class="logout" id="logout" name="logout" value="Logout">
    	</form>
    	
    	<div class = "search">
        	<form action="${pageContext.servletContext.contextPath}/search" method="post">
            	<input type="text" id="searchBox" name="search" value="${searchParameter}">
            	<button id= "searchBTN">&#128269</button>
        	</form>
    	</div>
    	
    	<p id="key">
			&#128196 = Submitted </br>
			&#11088 = New </br>
			&#9989 = Signed </br>
			&#10071 = Edited by Admin			
		</p>
    
    	<c:if test="${! empty tutorName}">
    		<div class="successAdd">${tutorName} was added as a tutor</div>
		</c:if>

		<c:if test="${! empty editTutorName}">
			<div class="successAdd">${editTutorName} was sucessfully updated</div>
		</c:if>

    	<div class="table">
    	<table id = "searchResults">
    		<th>Pay Vouchers</th>
        	<th>Tutor Name</th>
        	<th>Subject</th>
        	<th>Status</th>
        	<th>Due Date</th>
        	</tr>
       	 	<c:forEach items="${payVouchers}" var="voucher">
            	<tr>
					<td>
						<form action="${pageContext.servletContext.contextPath}/search" method="post">
							<input type="submit" id="view" name="viewPayVoucher" value="View"></input>
							<input type="hidden" name="ID" value="${voucher.right.payVoucherID}"/>
						</form>
					</td>
                	<td> ${voucher.left.name} </td>
                	<td> ${voucher.left.subject} </td>
                	<td>
                		<c:if test="${voucher.right.isSubmitted}">
                			&#128196
                		</c:if>
						<c:if test="${voucher.right.isNew}">
                			&#11088
                		</c:if>
                		<c:if test="${voucher.right.isSigned}">
                			&#9989
                		</c:if>
                		<c:if test="${voucher.right.isAdminEdited}">
                			&#10071
                		</c:if>
					</td>
                	<td> ${voucher.right.dueDate}</td>
           	 	</tr>
       		 </c:forEach>
    	</table>
    	</div>
    	
    	<c:if test="${! empty errorMessage}">
        	<div class="error">${errorMessage}</div>
        </c:if>

    	<div Class = "pageNum">
        	<p> <<< &#160; Page # &#160; >>></p>
   	 	</div>
    
    		<c:if test="${user.isAdmin}">
    		<div id="assignVoucher">
				<form action="${pageContext.servletContext.contextPath}/search" method="post">
					<input type="submit" id="assignVoucherBtn" name="assignVoucher" value="Assign Pay Voucher"></input></br></br></br></br>
					<label for="startDate" class="date1" id="assignVoucherDate">Start Date:</label>
		        	<input type="text" class="date1" id="assignVoucherText" name="startDate" placeholder="MM/DD/YYYY" value="${startDate}"></input>
		        	<label for="startDate" class="date2" id="assignVoucherDate">Due Date:</label>
		        	<input type="text" class="date2" id="assignVoucherText" name="dueDate" placeholder="MM/DD/YYYY" value="${dueDate}"></input>
		        	</br>
		        	<c:if test="${! empty assignErrorMessage}">
        				<div class="error">${assignErrorMessage}</div>
        			</c:if>
					<div id="radio">
						<input type="radio" id="assign-all" name="assign" value="allTutors" checked>
						<label for="assign-all" id="radio-label">All Tutors</label>
						<input type="radio" id="assign-one" name="assign" value="oneTutor">
						<label for="assign-one" id="radio-label">Specific Tutor:</label>
						<input type="text" id="edit-tutor-name" name="assignName" placeholder="Tutor Name" value="${assignName}"></input>
					</div>
		    	</form>
			</div>
		</c:if>
	
		<c:choose>
			<c:when test="${user.isAdmin}">
				<form action="${pageContext.servletContext.contextPath}/search" method="get">
					<input type="submit" id="addTutor" name="addTutor" value="Add Tutor" class="addTutor"></input>
					</br></br>
				</form>
			</c:when>
			<c:otherwise>
				<form action="${pageContext.servletContext.contextPath}/search" method="post">
					<input type="submit" id="addTutor" name="tutorProfile" value="Tutor Profile" class="addTutor"></input>
					</br></br>
				</form>
			</c:otherwise>
		</c:choose>

		<form action="${pageContext.servletContext.contextPath}/search" method="post">
			<c:if test="${user.isAdmin}">
				<input type="submit" id="editTutor" name="editTutor" value="Edit Tutor" class="addTutor"></input>
				<input type="text" class="editTutorText" id="edit-tutor-name" name="editTutorName" placeholder="Tutor Name" value="${editTutorName}"></input>
			</c:if>
		</form>	

		<c:if test="${user.isAdmin}">
			</br></br></br></br></br></br></br></br>
		</c:if>
		
	</div>

</body>

</html>