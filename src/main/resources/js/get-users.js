$(function () {
    var jiraAddress = 'http://localhost:2990/jira/'
    $.get(jiraAddress + 'rest/cab/1.0/systems/getactiveusers', function (response) {
        const arrayUsers = response.substring(1, response.length - 1).split(", ");
        const stages = ["#stage1", "#stage21", "#stage22", "#stage23", "#stage3","#authorize"]
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