$(document).ready(function () {
    var jiraURL = $(location).attr("href").split("secure")[0]
    var jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
    var method = "post"
    $('#form').attr('action', jiraRestAddress + method)
    $(document.body).on("click", "#bulk-edit", function () {
        var toggle = document.getElementById('bulk-edit')
        if (toggle.checked === true)    {
            $('#form').attr('action', jiraRestAddress + "postmulti")
        }
        else    {
            $('#form').attr('action', jiraRestAddress + method)
        }
    })
})