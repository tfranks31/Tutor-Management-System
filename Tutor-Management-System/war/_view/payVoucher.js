var i;
var tableSize = 10;

document.addEventListener("DOMContentLoaded", function () {
    for (i = 0; i < tableSize; i++) {
        addRow();
    }
});

var index = 0;
function addRow() {
    if (row + index < 20){
        var table = document.getElementById("voucherInfo");
        var row = table.insertRow(table.rows.length);
        var cell1 = row.insertCell(0);
        var t1 = document.createElement("input");
        t1.id = "txtDate" + index;
        cell1.appendChild(t1);
        var cell2 = row.insertCell(1);
        var t2 = document.createElement("input");
        t2.id = "txtHours" + index;
        cell2.appendChild(t2);
        var cell3 = row.insertCell(2);
        var t3 = document.createElement("input");
        t3.id = "txtServicePerformed" + index;
        cell3.appendChild(t3);
        var cell4 = row.insertCell(3);
        var t4 = document.createElement("input");
        t4.id = "txtLocationPerformed" + index;
        cell4.appendChild(t4);
        index++;
    }
}