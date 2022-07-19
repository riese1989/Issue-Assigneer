$(function () {
    var toggle = document.getElementById('bulk-edit')
    $(document.body).on("click", toggle, function () {
        if (toggle.checked === true)    {
            $('#divSystemCabSingle').attr("hidden","hidden")
            $('#divSystemCabMulti').removeAttr("hidden")
            $('#divTypeChangeSingle').attr("hidden","hidden")
            $('#divTypeChangeMulti').removeAttr("hidden")
            $(".checkbox").attr("hidden","hidden")
            $("#link-hide-table").attr("hidden","hidden")
            $("#link-return-values").attr("hidden","hidden")
            $("#table-history").attr("hidden","hidden")
        }   else    {
            $('#divSystemCabSingle').removeAttr("hidden")
            $('#divSystemCabMulti').attr("hidden","hidden")
            $('#divTypeChangeSingle').removeAttr("hidden")
            $('#divTypeChangeMulti').attr("hidden","hidden")
            $(".checkbox").removeAttr("hidden")
            $("#link-hide-table").removeAttr("hidden")
            $("#link-return-values").removeAttr("hidden")
            $("#table-history").removeAttr("hidden")
        }
    })
})