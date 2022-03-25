$(function () {
    const system = document.getElementById("systemCab")
    const typechange = document.getElementById("typechange")
    var countChecked = 0;
    var markedCheckbox = document.getElementsByName("checkbox")
    for (let checkbox of markedCheckbox) {
        if (checkbox.checked)
            countChecked++
    }
    if (system.value !== "0" && typechange.value !== "0" && countChecked !== 0) {
        console.log("Unblock")
    }
    else    {
        console.log("Block")
    }
})