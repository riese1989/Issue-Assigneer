$(function () {
    var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
    var jiraURL = currentURL.split("secure")[0]
    const jiraRestAddressTitle = jiraURL + 'rest/cab/1.0/systems/stage/title?namestage='
    const jiraRestAddressLabel = jiraURL + 'rest/cab/1.0/systems/stage/label?namestage='
    const nameElems = ["stage1", "stage21", "stage22", "stage23", "stage3", "authorize", "delivery", "active"]
    $.each(nameElems, function (i, n) {
        $.get(jiraRestAddressLabel + n, function (response) {
            $('#' + n + 'Label').text(response)
        })
        $('#' + n + 'LabelMulti').text("Не учитывать")
        $.get(jiraRestAddressTitle + n, function (response) {
            $('#' + n + 'Label').attr('title', response)
        })
    })
})