$(function () {
    const system = document.getElementById("systemCab")
    system.addEventListener('change', (event) => {
        const active = document.getElementById("active")
        const jiraRestAddress = 'http://localhost:2990/jira/rest/cab/1.0/systems/'
        $.get(jiraRestAddress + 'isactive?namesystem=' + system.value, function (response){
            active.value = response
        })
    })
})