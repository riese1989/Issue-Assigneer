$(function () {
    $('.checkboxMulti').change(function () {
        var idElem = this.id.replaceAll("CheckMulti","")
        var elem = $("#" + idElem)
        if(elem.is(':disabled'))    {
            elem.prop("disabled", false)
        }
        else    {
            elem.prop("disabled", true)
        }
    })
})