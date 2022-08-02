$(function () {
    $('.checkboxMulti').change(function () {
        setTimeout(enableFields, 200)
    })
})


function enableFields() {
    $('.checkboxMulti').each(function () {
        var idElem = this.id.replaceAll("CheckMulti", "")
        var elem = $("#" + idElem)
        if (this.checked) {
            elem.prop("disabled", true)
        }
        else    {
            elem.prop("disabled", false)
        }
    })
}