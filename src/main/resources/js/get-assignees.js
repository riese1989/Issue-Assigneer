$(function () {
    const jiraRestAddress = 'http://localhost:2990/jira/rest/cab/1.0/systems/'
    document.querySelectorAll(".select").forEach(function (item)    {
        item.addEventListener("change", event => {
            const system = document.getElementById("systemCab")
            const typechange = document.getElementById("typechange")
            const stage1 = document.getElementById("stage1")
            const stage21 = document.getElementById("stage21")
            const stage22 = document.getElementById("stage22")
            const stage23 = document.getElementById("stage23")
            const stage3 = document.getElementById("stage3")
            const authorize = document.getElementById("authorize")
            if(system.value !== "Select" && typechange.value !== "Select") {
                var url = jiraRestAddress + 'getassignees?namesystem=' + system.value + '&typechange=' + typechange.value + "&stage="
                $.get(url + 'stage1', function (response)   {
                    stage1.value = response
                })
                $.get(url + 'stage21', function (response)   {
                    stage21.value = response
                })
                $.get(url + 'stage22', function (response)   {
                    stage22.value = response
                })
                $.get(url + 'stage23', function (response)   {
                    stage23.value = response
                })
                $.get(url + 'stage3', function (response)   {
                    stage3.value = response
                })
                $.get(url + 'authorize', function (response)   {
                    authorize.value = response
                })
            }   else    {
                stage1.value = ""
                stage21.value = ""
                stage22.value = ""
                stage23.value = ""
                stage3.value = ""
                authorize.value = ""
            }
        })
    })

    function getdata (url) {
        var result = $.get(url)
        return result;
    }
})