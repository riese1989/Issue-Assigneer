$(function () {
    var systemCab = document.getElementById("systemCab");
    var countCheckedAfter = 0;
    var markedCheckbox = document.getElementsByName("checkbox")
    for (let checkbox of markedCheckbox) {
        if (checkbox.checked)
            countCheckedAfter++
    }
    console.log(countCheckedAfter)
    if (countCheckedAfter === 0)    {
        var typeChange = document.getElementById("typechange")
        typeChange.selectedIndex = 0
        systemCab.selectedIndex = 0
        $('.select').trigger('change')
    }
})

var systemCab = document.getElementById("systemCab")
if (system.value !== '0')    {
    console.log(1)
}
else    {
    console.log(2)
}

console.log($('#delivery').value)

$('#delivery').val(undefined).trigger('change')
$('#save').prop('disabled', false);
$('#delivery').prop('disabled', false);
$()