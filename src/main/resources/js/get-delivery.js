$(function () {
    $(document.body).on("change", "#systemCab", function () {
        $('#delivery').val('0').change()
        const system = document.getElementById("systemCab")
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        if (system.value !== '0') {
            $.get(jiraRestAddress + 'delivery?idsystem=' + system.value, function (response) {
                $('#delivery').val(response).trigger('change')
            })
        }
    })
})