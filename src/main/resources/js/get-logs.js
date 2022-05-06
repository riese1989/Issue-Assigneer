$(function () {
    $(document.body).on("change", ".select", function () {
        $('.table-history tbody').empty()
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        const system = document.getElementById("systemCab")
        const typechange = document.getElementById("typechange")
        if (system.value !== '' && system.value !== undefined && system.value !== '0' &&
            typechange.value !== '' && typechange.value !== undefined && typechange.value !== '0')  {
            $.get(jiraRestAddress + 'getlogs?idsystem=' + system.value + '&idtypechange=' + typechange.value, function (response)   {
                $('.table-history tbody').append(response)
            })
        }
    })
})