$(function () {
    var jiraAddress = 'http://localhost:2990/jira/'
    $.get(jiraAddress + 'rest/cab/1.0/systems/getlistsystems', function (response) {
        const arraySystems = response.substring(1, response.length - 1).split(", ");
        arraySystems.forEach((system) => {
            var newOption = document.createElement("option");
            newOption.innerHTML = system
            newOption.value = system
            document.querySelector('#systemCab').append(newOption)
        })
    })
})