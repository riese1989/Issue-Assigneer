$(function () {
    $(document.body).on("change", "#systemCab", function () {
        const system = document.getElementById("systemCab")
        const jiraRestAddress = 'http://localhost:2990/jira/rest/cab/1.0/systems/'
        if (system.value !== "0") {
            $.get(jiraRestAddress + 'isactive?namesystem=' + system.value, function (response) {
                $('#active').val(response).trigger('change')
            })
        }
    })
})