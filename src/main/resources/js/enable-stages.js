$(function () {
    $(document.body).on("change", ".select", function () {
        const typechange = document.getElementById("typechange")
        const system = document.getElementById("systemCab")
        if (system.value !== "0" && typechange.value !== "0")    {
            $(".multiselect").prop("disabled", false);
            $(".selectdown").prop("disabled", false);
            $('#save').prop('disabled', false);
        }   else    {
            $(".multiselect").prop("disabled", true);
            $(".selectdown").prop("disabled", true);
            $('#save').prop('disabled', true);
        }
    });
})

