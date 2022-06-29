$(function () {
    $(document.body).on("change", ".select", function () {
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        const system = document.getElementById("systemCab")
        const typechange = document.getElementById("typechange")
        const stages = ["stage1", "stage21", "stage22", "stage23", "stage3", "authorize", "delivery"]
        $.each(stages, function (idStage, stage) {
            $.get(jiraRestAddress + 'getIa?namesystem=' + system.value + '&typechange=' + typechange.value + "&stage=" + stage, function (response) {
                const arrayUsers = response.substring(1, response.length - 1).split(", ")
                $.each(arrayUsers, function (idUser, user) {
                    var newOption = document.createElement("option")
                    newOption.innerHTML = user.split("=")[0]
                    newOption.value = user.split("=")[1]
                    document.querySelector("#" + stage).append(newOption)
                })
            })
        });
    })
})