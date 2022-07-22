$(function () {
    $(document.body).on("change", "#systemCab", function () {
        var toggle = document.getElementById('bulk-edit')
        if (toggle === false) {
            const system = document.getElementById("systemCab")
            var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
            var jiraURL = currentURL.split("secure")[0]
            const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
            if (system.value !== "0") {
                $.get(jiraRestAddress + 'isactive?namesystem=' + system.value, function (response) {
                    $('#active').val(response).trigger('change')
                })
            }
        }
    })
})