<!DOCTYPE html>
<html>
    
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search View</title>
    <link rel="stylesheet" href="./_view/search.css">
</head>

<script src="./_view/search.js"></script>

<body>

    <h1 class = "Header"> Search</h1>
    
    <div class = "search">
        <form>
            <input type="text">
            </br>
            <button id="searchBTN">Search</button>
        </form>
    </div>
    
    <form action="${pageContext.servletContext.contextPath}/search" method="get">
        <input type="submit" name="addTutor" value="Add Tutor" class="addTutor"></input>
    </form>

    <table id = "searchResults">
        <th>Tutor Name</th>
        <th>Subject</th>
        <th>Voucher Submitted</th>
        <th>Date Submitted</th>
        </tr>
    </table>

    <div Class = "pageNum">
        <p> <<< &#160; Page # &#160; >>></p>
    </div>
</body>

</html>