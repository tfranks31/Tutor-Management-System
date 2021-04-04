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
        <form action="${pageContext.servletContext.contextPath}/search" method="get">
            <input type="text" name="search" value="${searchParameter}">
            </br>
            <button id= "searchBTN">Search</button>
        </form>
    </div>
    
    <form action="${pageContext.servletContext.contextPath}/search" method="get">
        <input type="submit" name="addTutor" value="Add Tutor" class="addTutor"></input>
    </form>
    
    <table id = "searchResults">
        <th>Tutor Name</th>
        <th>Subject</th>
        <th>Voucher Submitted</th>
        <th>Due Date</th>
        </tr>
        <c:forEach items="${payVouhcers}" var="voucher">
            <tr>
                <td> TO DO </td>
                <td> To Do </td>
                <td> ${voucher.isSubmitted} </td>
                <td> ${voucher.dueDate}</td>
            </tr>
        </c:forEach>
    </table>


    <div Class = "pageNum">
        <p> <<< &#160; Page # &#160; >>></p>
    </div>
</body>

</html>