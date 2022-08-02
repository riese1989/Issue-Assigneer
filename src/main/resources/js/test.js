
var jiraURL = $(location).attr("href").split("secure")[0]
var jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
$.get( jiraRestAddress + "getcad?idSystems=1,2", function (response)    {
    console.log(response)
});

$('#descriptionDelivery').text("178219749134")


$("#active option[value='-1']").wrap("<span/>")