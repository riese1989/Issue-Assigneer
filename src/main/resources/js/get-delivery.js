$(function () {
    $(document.body).on("change", "#systemCab", function () {
        const system = document.getElementById("systemCab")
        const jiraRestAddress = 'http://localhost:2990/jira/rest/cab/1.0/systems/'
        $.get(jiraRestAddress + 'delivery?idsystem=' + system.value, function (response){
            $('#delivery').val(response).trigger('change')
        })
    })
})