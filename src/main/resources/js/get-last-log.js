$(function () {
    $(document.body).on("click", "#link-return-values", function () {
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        const system = document.getElementById("systemCab")
        const stages = ["stage1", "stage21", "stage22", "stage23", "stage3", "authorize", "delivery", "active"]
        const typechange = document.getElementById("typechange")
        if (system.value !== '' && system.value !== undefined && system.value !== '0' &&
            typechange.value !== '' && typechange.value !== undefined && typechange.value !== '0')  {
            $.get(jiraRestAddress + 'getlastlogs?idsystem=' + system.value + '&idtypechange=' + typechange.value, function (response)   {
                console.log(response)
                $.each(stages, function (idStage, stage) {
                    // if (stage !== "delivery") {
                    //     var values = response.split(stage + "=[")[1].split("]")[0].split(", ")
                    //     $('#'+stage).val(values).trigger('change')
                    // }
                    // if (stage === "delivery")   {
                    //     var preVal = response.split(/delivery=/, )[1]
                    //     console.log(preVal)
                    //     var value = preVal.split(", ")[0]
                    //     $('#delivery').val(value).trigger('change')
                    // }
                    // if (stage === "active")   {
                    //     var value = response
                    //     $('#active').val(value).trigger('change')
                    // }

                })
            })
        }
    })
})