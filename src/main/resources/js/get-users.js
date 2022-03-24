$(function () {
    var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
    var jiraURL = currentURL.split("secure")[0]
    const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
    $.get(jiraRestAddress + 'getactiveusers', function (response) {
        const arrayUsers = response.substring(1, response.length - 1).split(", ");
        const stages = ["#stage1", "#stage21", "#stage22", "#stage23", "#stage3","#authorize", "#delivery"]
        stages.forEach((stage) =>{
            arrayUsers.forEach((user) => {
                var newOption = document.createElement("option");
                newOption.innerHTML = user.split("=")[0]
                newOption.value = user.split("=")[1]
                document.querySelector(stage).append(newOption)
            })
        })
    })
})