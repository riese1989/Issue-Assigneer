$(function () {
    $(document.body).on("change", ".select", function () {
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        const system = document.getElementById("systemCab")
        const typechange = document.getElementById("typechange")
        if (system.value !== "0" && typechange.value !== "0" && system.value !== "" && typechange.value !== "") {
            var url = jiraRestAddress + 'getassignees?namesystem=' + system.value + '&typechange=' + typechange.value + "&stage="
            $.get(url + 'stage1', function (response) {
                $('#stage1').val(response.split(", ")).trigger('change')
            })
            $.get(url + 'stage21', function (response) {
                $('#stage21').val(response.split(", ")).trigger('change')
            })
            $.get(url + 'stage22', function (response) {
                $('#stage22').val(response.split(", ")).trigger('change')
            })
            $.get(url + 'stage23', function (response) {
                $('#stage23').val(response.split(", ")).trigger('change')
            })
            $.get(url + 'stage3', function (response) {
                $('#stage3').val(response.split(", ")).trigger('change')
            })
            $.get(url + 'authorize', function (response) {
                $('#authorize').val(response.split(", ")).trigger('change')
            })
        } else {
            $('.multiselect').val(null).trigger('change')
            $('#authorize').val(null).trigger('change')
        }
        if ($('.table-history').is(":visible")) {
            $("#link-hide-table").click()
        }
    })
})