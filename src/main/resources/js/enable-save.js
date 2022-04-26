$(function () {
    $(document.body).on("change", ".select", function () {
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        const system = document.getElementById("systemCab")
        const typechange = document.getElementById("typechange")
        var countChecked = $('.checkboxes:checked').size()
        if (system.value !== '' && system.value !== undefined && system.value !== '0' &&
            typechange.value !== '' && typechange.value !== undefined && typechange.value !== '0') {
            $.get(jiraRestAddress + 'isenable?idsystem=' + system.value + '&idtypechange=' + typechange.value, function (response) {
                if (system.value !== "0" && typechange.value !== "0" && countChecked !== 0 && response === "true") {
                    $('#save').prop('disabled', false)
                } else {
                    $('#save').prop('disabled', true)
                }
            })
        }   else    {
            $('#save').prop('disabled', true)
        }
    });
})