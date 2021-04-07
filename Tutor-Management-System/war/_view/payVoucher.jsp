<!DOCTYPE html>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pay Voucher View</title>
    <link rel="stylesheet" href="./_view/payVoucher.css">
</head>

<body>

    <h1 class = "Header"> Pay Voucher</h1>
    
    <div class ="studentInfo">
        Tutor name:  ${tutorName}<br />
        Student ID#: ${studentID}
    </div>
	
    <div class = "dueDate">
        DueDate: ${dueDate}
    </div>
    <form action="${pageContext.servletContext.contextPath}/payVoucher"method="post" >
        <table>   
            <th>Date</th>
            <th>Hours</th>
            <th>Service Performed</th>
            <th>Where Performed</th>
            <c:forEach items="${entries}" var="entry">
		        <tr>
		            <td><input type="text" name="cell" value="${entry.date}"></td>
		            <td><input type="text" name="cell" value="${entry.hours}"></td>
		            <td><input type="text" name="cell" value="${entry.servicePerformed}"></td>
		            <td><input type="text" name="cell" value="${entry.wherePerformed}"></td>			            
		        </tr>
		    </c:forEach>
            <c:forEach var="i" begin="1" end="${tableSize}" step="1">
            	<tr>
            		<c:forEach var="j" begin="1" end="4" step="1">
            			<td><input type="text" name="cell"></td>
            		</c:forEach>
            	</tr>
            </c:forEach>
           
       	</table>
        </br><input type="submit" id="addRow" name="addRow" value="Add Row">
  
		<div class = "payInfo">
		    <p> 
		       Account Number = ${accountNumber}&#160;&#160;&#160; 
		       Total Hours = ${totalHours}&#160;&#160;&#160; 
		       Pay Rate = $ ${payRate}&#160;&#160;&#160;
		       Total Pay = $ ${totalPay}
		    </p>
		</div>
		
		<input type="submit" class="submit" id="update" name="updateVoucher" value="Update Pay Voucher">
		
		<c:choose>
			<c:when test="${user.isAdmin}">
				<input type="submit" class="sign" id="sign" name="signVoucher" value="Sign Pay Voucher">
			</c:when>
			<c:otherwise>
				<input type="submit" class="submit" id="submit" name="submitVoucher" value="Submit Pay Voucher">
			</c:otherwise>
		</c:choose>
		
    </form>
	<form action="${pageContext.servletContext.contextPath}/payVoucher" method="get">
   	     <input type="submit" id="back" name="back" value="Back">
   	</form>
   	
   	</br>
</body>

</html>