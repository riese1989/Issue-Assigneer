$(function () {
    $(document.body).on("change", ".select", function () {
        var toggle = document.getElementById('bulk-edit')
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        var system = document.getElementById("systemCab")
        const typechange = document.getElementById("typechange")
        if (system.value !== "0" && typechange.value !== "0" && system.value !== "" && typechange.value !== "") {
            const stages = ["stage1", "stage21", "stage22", "stage23", "stage3", "authorize", "delivery"]
            $.each(stages, function (idStage, stage) {
                $.get(jiraRestAddress + 'getIa?namesystem=' + system.value + '&typechange=' + typechange.value + "&stage=" + stage, function (response) {
                    const arrayUsers = response.split(", ")
                    $.each(arrayUsers, function (idUser, user) {
                        var newOption = document.createElement("option")
                        newOption.innerHTML = user.split("=")[0]
                        newOption.value = user.split("=")[1]
                        document.querySelector("#" + stage).append(newOption)
                    })
                })
            });
        }
        if (system.value !== "0" && typechange.value === "0" && toggle.checked === false && system.value !== '')   {
            $.get(jiraRestAddress + "delivery?idsystem=" + system.value, function (deliveryIdResponse){
                $.get(jiraRestAddress + "getuser?id=" + deliveryIdResponse, function (user){
                    var dataUser = user.split("=")
                    if(dataUser[2] === "false")
                    var newOption = document.createElement("option")
                    newOption.innerHTML = dataUser[0] + "[X]"
                    newOption.value = dataUser[1]
                    document.querySelector("#delivery").append(newOption)
                })
            })
        }
    })
})