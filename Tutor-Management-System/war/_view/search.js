var i;
var tableSize = 10;

document.addEventListener("DOMContentLoaded", function () {
    for (i = 0; i < tableSize; i++) {
        addRow2();
    }
});

var names = ["N", "I","C","K","E","L","B","A","C","K"];
var subjects = ["Liberal Arts", "Business", "Sports Management","radians",
                "physics", "Liberal Arts", "CS", "Art", "film", "ECE"];
var index = 0;

function addRow2() {
    var table = document.getElementById("searchResults");
    var row = table.insertRow(table.rows.length);
    var cell1 = row.insertCell(0)
    cell1.id = "tutorName" + index;
    cell1.innerHTML = names[index];
    var cell2 = row.insertCell(1)
    cell2.id = "subject" + index;
    cell2.innerHTML = subjects[index];
    var cell3 = row.insertCell(2)
    cell3.id = "submitted" + index;
    cell3.innerHTML = "yes";
    var cell4 = row.insertCell(3);
    cell4.id = "dateSubmitted" + index;
    cell4.innerHTML = "March " + (index + 1);
    index++;
}