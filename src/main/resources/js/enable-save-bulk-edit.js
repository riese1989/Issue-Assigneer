$(function () {
    $(document.body).on("change", "#systemCabMulti, #typechangeMulti", function () {
        if ($("#systemCabMulti").val() !== null && $("#typechangeMulti").val() !== null)    {
            $('#save').prop('disabled', false)
            $('#active').prop('disabled', false)
        }
        else    {
            $('#save').prop('disabled', true)
        }
    });
})