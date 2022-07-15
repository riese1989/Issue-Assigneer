$(function () {
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        $.get(jiraRestAddress + 'isenablebulk', function (response) {
            if (response === "true") {
                $('#bulk-edit').removeAttr("hidden")
                $('#bulk-edit-label').removeAttr("hidden")
            } else {
                $('#bulk-edit').attr("hidden","hidden")
                $('#bulk-edit-label').attr("hidden","hidden")
            }
        })
})