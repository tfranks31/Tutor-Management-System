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
        Tutor name: <br />
        Student ID#:
    </div>

	<div class = "back">
		<form action="${pageContext.servletContext.contextPath}/payVoucher" method="get">
   	     	<input type="submit" name="back" value="Back">
   		 </form>
	</div>
	
    <div class = "dueDate">
        DueDate:
    </div>
        <form action="${pageContext.servletContext.contextPath}/payVoucher"method="post" >
            <table>   
                <th>Date</th>
                <th>Hours</th>
                <th>Service Preformed</th>
                <th>Where Preformed</th>
                </tr>
                <c:forEach var = "counter" begin = "1" end = "${tableSize}" step = "1">
                	<tr>
                		<c:forEach var = "counter2" begin = "1" end = "4" step = "1">
                			<td><input type="text" name="cell"></td>
                		</c:forEach>
                	</tr>
                </c:forEach>
               
                </table>
                <input type="submit" name="addRow" value="Add Row">
    </form>
  
    <div class = "payInfo">
        <p> 
           Account Number = ##&#160;&#160;&#160; 
           Total Hours = ##&#160;&#160;&#160; 
           Pay Rate = $7.25&#160;&#160;&#160;
           Total Pay = ### 
        </p>
    </div>

    <button class="submit">Submit Pay Voucher</button>


</body>

</html>