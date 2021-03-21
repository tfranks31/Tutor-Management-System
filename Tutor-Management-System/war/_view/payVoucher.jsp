<!DOCTYPE html>
<html>
      
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pay Voucher View</title>
    <link rel="stylesheet" href="./_view/payVoucher.css">
</head>

<script type="text/javascript" src="./_view/payVoucher.js"></script>

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
        <form action="${pageContext.servletContext.contextPath}/payVoucher"method="post">
            <table id = "voucherInfo">
                <th>Date</th>
                <th>Hours</th>
                <th>Service Preformed</th>
                <th>Where Preformed</th>
                </tr>
            </table>
    </form>

    <button class = "addRow" onclick="addRow()">Add Row</button>    

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