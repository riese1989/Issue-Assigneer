$(function () {
    var toggle = document.getElementById('bulk-edit')
    $(document.body).on("click", toggle, function () {
        if (toggle.checked === true)    {
            $('#systemCab').attr('multiple')
        }
    })
})