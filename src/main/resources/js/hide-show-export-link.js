$(document).ready(function () {
    $('.checkboxes').change(function () {
        var flag = false
        $('input[name="checkbox"]:checked').each(function() {
            if (this.value === "4") {
                flag = true
            }
        });
        if (flag) {
            var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
            var jiraURL = currentURL.split("secure")[0]
            const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
            $.get(jiraRestAddress + "countmysystems", function (response) {
                if (response !== "0") {
                    $('.exportButton').removeAttr("hidden")
                } else {
                    $(".exportButton").attr('hidden', '');
                }
            })
        }
        else {
            $(".exportButton").attr('hidden', '');
        }
    })
})