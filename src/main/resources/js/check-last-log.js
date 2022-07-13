$(function () {
    $(document.body).on("change", ".select", function () {
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/checklastlog'
        const system = document.getElementById("systemCab")
        const typechange = document.getElementById("typechange")
        if (system.value !== "0" && typechange.value !== "0" && system.value !== "" && typechange.value !== "") {
            var url = jiraRestAddress + '?idsystem=' + system.value + '&idtypechange=' + typechange.value
            $.get(url, function (response) {
                if(response === "true")   {
                    $('#link-return-values').removeAttr("hidden")
                }   else    {
                    $('#link-return-values').attr('hidden','');
                }
            })
        }
    })
})