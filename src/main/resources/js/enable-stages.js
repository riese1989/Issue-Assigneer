$(function () {
    $(document.body).on("change", ".select", function () {
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        const system = document.getElementById("systemCab")
        const typechange = document.getElementById("typechange")
        if(system.value !== "0") {
            $.get(jiraRestAddress + 'isenable?idsystem=' + system.value, function (response) {
                if (system.value !== "0" && typechange.value !== "0" && response === "true") {
                    $(".multiselect").prop("disabled", false);
                    $(".selectdown").prop("disabled", false);
                    $('#save').prop('disabled', false);
                } else {
                    $(".multiselect").prop("disabled", true);
                    $(".selectdown").prop("disabled", true);
                    $('#save').prop('disabled', true);
                }
            })
        }
    });
})