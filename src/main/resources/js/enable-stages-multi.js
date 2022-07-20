$(function () {
    $(document.body).on("change", ".multiselect", function () {
        if( $("#systemCabMulti").val() !== null && $("#typechangeMulti").val() !== null) {
            $("#stage1").prop("disabled", false)
            $("#stage21").prop("disabled", false)
            $("#stage22").prop("disabled", false)
            $("#stage23").prop("disabled", false)
            $("#stage3").prop("disabled", false)
            $("#authorize").prop("disabled", false)
            $("#delivery").prop("disabled", false)
            $("#active").prop("disabled", false)
        }
        else    {
            $("#stage1").prop("disabled", true)
            $("#stage21").prop("disabled", true)
            $("#stage22").prop("disabled", true)
            $("#stage23").prop("disabled", true)
            $("#stage3").prop("disabled", true)
            $("#authorize").prop("disabled", true)
            $("#delivery").prop("disabled", true)
            $("#active").prop("disabled", true)
        }
    })
})