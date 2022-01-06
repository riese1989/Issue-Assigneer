$(function () {
    const jiraRestAddress = 'http://localhost:2990/jira/rest/cab/1.0/'
    document.querySelectorAll(".select").forEach(function (item)    {
        item.addEventListener("change", event => {
            const system = document.getElementById("systemCab")
            const typechange = document.getElementById("typechange")
            if(system.value != "Select" && typechange.value != "Select") {
                const step1 = document.getElementById("step1")
                const step21 = document.getElementById("step21")
                const step22 = document.getElementById("step22")
                const step23 = document.getElementById("step23")
                const step3 = document.getElementById("step3")
                const autorize = document.getElementById("autorize")
                const active = document.getElementById("active")
                var url = jiraRestAddress + 'getassignees?namesystem=' + system.value + '&typechange=' + typechange.value + "&stage="
                step1.value = getdata(url + 'step1')
            }
        })
    })

    function getdata (url) {
        var result;
        $.get(url, function (response) {
            result = response;
        })
        return result;
    }
})