<!DOCTYPE html>
<html>
      
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pay Voucher View</title>
    <link rel="stylesheet" href="./_view/payVoucherMain.css">
</head>

<script src="./_view/payVoucher.js"></script>

<body>

    <h1 class = "Header"> Pay Voucher</h1>
    
    <div class ="studentInfo">
        Tutor name: <br />
        Student ID#:
    </div>

    <div class = "dueDate">
        DueDate:
    </div>
    
    <table id = "voucherInfo">
        <th>Date</th>
        <th>Hours</th>
        <th>Service Preformed</th>
        <th>Where Preformed</th>
        </tr>
    </table>

    <button class = "addRow" onclick="addRow()">Add Row</button>
  
    <div class = "payInfo">
        <p> 
           Account Number = ##&#160;&#160;&#160; 
           Total Hours = ##&#160;&#160;&#160; 
           Pay Rate = $7.25&#160;&#160;&#160;
           Total Pay = ### 
        </p>
    </div>

    <button class="submit" onclick="addRow()">Submit Pay Voucher</button>

</body>

</html>