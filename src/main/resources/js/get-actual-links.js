$(document).ready(function() {
    var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
    var jiraURL = currentURL.split("secure")[0]
    const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/export'
    $("#save").attr("href", jiraURL)
    $("#cancel").attr("href", jiraRestAddress)
    console.log(jiraRestAddress)
})