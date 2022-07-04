var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
var jiraURL = currentURL.split("secure")[0]
const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
$(".multiselect").empty();
$.get(jiraRestAddress + 'getusers', function (response) {
    const arrayUsers = response.substring(1, response.length - 1).split(", ")
    $.each(arrayUsers, function (idUser, user)   {
        var option = $('<option></option>')
            .attr('value', user.split("=")[1])
            .text(user.split("=")[0])
            .prop('selected', false);
        $('.multiselect').append(option).change();
        })
});
