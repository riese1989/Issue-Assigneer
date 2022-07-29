$(function () {
    $(document.body).on("change", "#systemCab", function () {
        var toggle = document.getElementById('bulk-edit')
        if (toggle.checked === false) {
            setTimeout(getDelivery, 200)
        }
    })
})

function getDelivery() {
    var system = document.getElementById("systemCab")
    if (system.value !== '0') {
        $('#delivery').val('0').change()
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        $.get(jiraRestAddress + 'delivery?idsystem=' + system.value, function (response) {
            $('#delivery').val(response).trigger('change')
        })
    }
}