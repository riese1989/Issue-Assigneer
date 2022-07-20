$(document).ready(function() {
    $('#systemCabMulti').empty()
    var jiraURL = $(location).attr("href").split("secure")[0]
    var jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
    $.get(jiraRestAddress + 'getmysystems', function (response) {
        var jsonArray = JSON.parse(response)
        for (var i = 0; i < jsonArray.length; i++) {
            var name = jsonArray[i].name
            var id = jsonArray[i].id
            $('#systemCabMulti').append(new Option(name, id))
        }
    })
})