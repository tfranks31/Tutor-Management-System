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
            <input type="text" id="searchBox" name="search" value="${searchParameter}">
            <button id= "searchBTN">&#128269</button>
        </form>
    </div>
    	
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
						<input type="submit" id="view" name="viewPayVoucher" value="View"></input>
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
				<input type="submit" id="assignVoucherBtn" name="assignVoucher" value="Assign Pay Voucher"></input></br></br></br>
				<label for="startDate" id="assignVoucherDate">Start Date:</label>
		        <input type="text" id="assignVoucherText" name="startDate" placeholder="MM/DD/YYYY" value="${startDate}"></input></br></br>
		        <label for="startDate" id="assignVoucherDate">Due Date:</label>
		        <input type="text" id="assignVoucherText" name="dueDate" placeholder="MM/DD/YYYY" value="${dueDate}"></input>
		    </form>
		</div>
	</c:if>
	
	<c:if test="${user.isAdmin}">
    	<form action="${pageContext.servletContext.contextPath}/search" method="get">
        	<input type="submit" id="addTutor" name="addTutor" value="Add Tutor" class="addTutor"></input>
        	</br></br></br></br></br></br></br></br></br></br>
    	</form>
	</c:if>
      
	<c:if test="${! empty tutorName}">
    	<div class="successAdd">${tutorName} was added as a tutor</div>
	</c:if>
	

    
</body>

</html>