$(document).ready(function() {
    $('#typechange').empty()
    $('#typechange').append(new Option("Select", "0"))
    var jiraURL = $(location).attr("href").split("secure")[0]
    var jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
    $.get(jiraRestAddress + 'getlisttypechanges', function (response) {
        var hashMapTypeChanges = response.substr(1, response.length - 2) + ""
        var typeChanges = hashMapTypeChanges.split(", ")
        $(typeChanges).each(function (index, typeChange){
            var typeCh = typeChange.split("=")
            $('#typechange').append(new Option(typeCh[1], typeCh[0]))
        })
    })
})