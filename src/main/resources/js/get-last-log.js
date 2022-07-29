$(function () {
    $(document.body).on("click", "#link-return-values", function () {
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        var system = document.getElementById("systemCab")
        const typechange = document.getElementById("typechange")
        if (system.value !== '' && system.value !== undefined && system.value !== '0' &&
            typechange.value !== '' && typechange.value !== undefined && typechange.value !== '0')  {
            $.get(jiraRestAddress + 'getlastlogs?idsystem=' + system.value + '&idtypechange=' + typechange.value, function (response)   {
                var obj = jQuery.parseJSON(response);
                if (obj.stage1 !== undefined)   {
                    $('#stage1').val(obj.stage1).trigger('change')
                }
                if (obj.stage21 !== undefined)   {
                    $('#stage21').val(obj.stage21).trigger('change')
                }
                if (obj.stage22 !== undefined)   {
                    $('#stage22').val(obj.stage22).trigger('change')
                }
                if (obj.stage23 !== undefined)   {
                    $('#stage23').val(obj.stage23).trigger('change')
                }
                if (obj.stage3 !== undefined)   {
                    $('#stage3').val(obj.stage3).trigger('change')
                }
                if (obj.authorize !== undefined)   {
                    $('#authorize').val(obj.authorize).trigger('change')
                }
                if (obj.delivery !== undefined)   {
                    $('#authorize').val(obj.delivery).trigger('change')
                }
            })
            $('#link-return-values').attr("hidden","hidden");
        }
    })
})