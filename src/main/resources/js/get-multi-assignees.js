$(function () {
    $(document.body).on("change", "#systemCabMulti, #typechangeMulti", function () {
        var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
        var jiraURL = currentURL.split("secure")[0]
        const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        var systemValue = $("#systemCabMulti").val()
        var typeChangeValue = $("#typechangeMulti").val()
        if (systemValue !== null && typeChangeValue !== null) {
            $('.checkMultiDiv').removeAttr("hidden")
            var url = jiraRestAddress + 'getassigneesmulti?idSystems=' + systemValue +
                '&idTypeChanges=' + typeChangeValue
            $.get(url, function (response) {
                $(".multiselect").prop("disabled", false)
                var obj = jQuery.parseJSON(response);
                if (obj.stage1 !== undefined) {
                    $('#stage1').val(obj.stage1).trigger('change')
                }
                if (obj.stage21 !== undefined) {
                    $('#stage21').val(obj.stage21).trigger('change')
                }
                if (obj.stage22 !== undefined) {
                    $('#stage22').val(obj.stage22).trigger('change')
                }
                if (obj.stage23 !== undefined) {
                    $('#stage23').val(obj.stage23).trigger('change')
                }
                if (obj.stage3 !== undefined) {
                    $('#stage3').val(obj.stage3).trigger('change')
                }
                if (obj.authorize !== undefined) {
                    $('#authorize').val(obj.authorize).trigger('change')
                }
                if (obj.delivery !== undefined && obj.delivery.length === 1)  {
                    $('#delivery').val(obj.delivery[0]).trigger('change')
                }   else {
                    $('#delivery').val("0").trigger('change')
                }
                if (obj.active !== undefined && obj.active.length === 1)    {
                    $('#active').val(obj.active).trigger('change')
                }
                else {
                    $('#active').val("-1").trigger('change')
                }
            })
        }
    })
})