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

    <h1 class = "Header"> Search</h1>
    
    <div class = "search">
        <form action="${pageContext.servletContext.contextPath}/search" method="post">
            <input type="text" name="search" value="${searchParameter}">
            </br>
            <button id= "searchBTN">Search</button>
        </form>
    </div>
    
    <c:if test="${user.isAdmin}">
    	<form action="${pageContext.servletContext.contextPath}/search" method="get">
        	<input type="submit" name="addTutor" value="Add Tutor" class="addTutor"></input>
    	</form>
	</c:if>
    
    
	<c:if test="${! empty tutorName}">
    	<div class="successAdd">${tutorName} was added as a tutor</div>
	</c:if>
    	
    <table id = "searchResults">
    	<th>Pay Vouchers</th>
        <th>Tutor Name</th>
        <th>Subject</th>
        <th>Voucher Submitted</th>
        <th>Due Date</th>
        </tr>
        <c:forEach items="${payVouchers}" var="voucher">
            <tr>
				<td>
					<form action="${pageContext.servletContext.contextPath}/search" method="post">
						<input type="submit" name="viewPayVoucher" value="View Pay Voucher"></input>
						<input type="hidden" name="ID" value="${voucher.right.payVoucherID}"/>
					</form>
				</td>
                <td> ${voucher.left.name} </td>
                <td> ${voucher.left.subject} </td>
                <td> ${voucher.right.isSubmitted} </td>
                <td> ${voucher.right.dueDate}</td>
            </tr>
        </c:forEach>
    </table>


    <div Class = "pageNum">
        <p> <<< &#160; Page # &#160; >>></p>
    </div>
    
    <c:if test="${user.isAdmin}">
    	<div id="assignVoucher">
			<form action="${pageContext.servletContext.contextPath}/search" method="post">
				<input type="submit" name="assignVoucher" value="Assign Pay Voucher"></input>
				<label for="startDate">Start Date:</label>
		        <input type="text" name="startDate" value="${startDate}"></input>
		        <label for="startDate">Due Date:</label>
		        <input type="text" name="dueDate" value="${dueDate}"></input>
		    </form>
		</div>
	</c:if>
    
</body>

</html>