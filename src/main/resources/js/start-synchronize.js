$(document).ready(function () {
    var jiraURL = $(location).attr("href").split("secure")[0]
    var jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
    $.get(jiraRestAddress + "synch")
})