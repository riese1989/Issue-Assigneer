$(function () {
    var jiraAddress = 'http://localhost:2990/jira/'
    $.get(jiraAddress + 'rest/cab/1.0/systems/getlistsystems', function (response) {
        let hashMapSystems = response.substr(1,response.length - 2);
        let mapSystems = hashMapSystems.split(", ")
        mapSystems.forEach((system) =>{
            var newOption = document.createElement("option");
            let dataSystem = system.split("=");
            newOption.innerHTML = dataSystem[1]
            newOption.value = dataSystem[0];
            document.querySelector('#systemCab').append(newOption)
        })
    })
})
