$(function () {
    $(document.body).on("click", "#bulk-edit", function () {
        var toggle = document.getElementById('bulk-edit')
        if (toggle.checked === true)    {
            $('#divSystemCabSingle').attr("hidden","hidden")
            $('#divSystemCabMulti').removeAttr("hidden")
            $('#divTypeChangeSingle').attr("hidden","hidden")
            $('#divTypeChangeMulti').removeAttr("hidden")
            $(".checkbox").attr("hidden","hidden")
            $("#link-hide-table").attr("hidden","hidden")
            $("#link-return-values").attr("hidden","hidden")
            $("#table-history").attr("hidden","hidden")
            $('.multiselect').val(null).trigger('change')
            $('#delivery').val(0).trigger('change')
            $('#save').prop('disabled', true)
            $("#stage1").prop("disabled", true)
            $("#stage21").prop("disabled", true)
            $("#stage22").prop("disabled", true)
            $("#stage23").prop("disabled", true)
            $("#stage3").prop("disabled", true)
            $("#authorize").prop("disabled", true)
            $("#systemCab").val(0).trigger('change')
            $('#systemCabMulti').empty()
            $('#active').prop('disabled', true)
            $('#active').append(new Option("Empty", "-1"))
            var jiraURL = $(location).attr("href").split("secure")[0]
            var jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
            $.get(jiraRestAddress + 'getmysystems', function (response) {
                var jsonArray = JSON.parse(response)
                for (var i = 0; i < jsonArray.length; i++) {
                    var name = jsonArray[i].name
                    var id = jsonArray[i].id
                    $('#systemCabMulti').append(new Option(name, id))
                }
            })
        }   else    {
            $('.checkMultiDiv').attr("hidden","hidden")
            $("#active option[value='-1']").remove()
            $('#systemCab').empty()
            $('#divSystemCabSingle').removeAttr("hidden")
            $('#divSystemCabMulti').attr("hidden","hidden")
            $('#divTypeChangeSingle').removeAttr("hidden")
            $('#divTypeChangeMulti').attr("hidden","hidden")
            $(".checkbox").removeAttr("hidden")
            $(".multiselect").val("").trigger('change')
            $("#link-hide-table").removeAttr("hidden")
            $("#table-history").removeAttr("hidden")
            $('#save').prop('disabled', false)
            $(".multiselect").prop("disabled", true)
            $(".select").val("0").change();
        }
    })
})