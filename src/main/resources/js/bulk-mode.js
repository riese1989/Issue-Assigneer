$(function () {
    $(document.body).on("click", "#bulk-edit", function () {
        var toggle = document.getElementById('bulk-edit')
        if (toggle.checked === true)    {
            $('#divSystemCabSingle').attr("hidden","hidden")
            $('#divSystemCabMulti').removeAttr("hidden")
            $('#divTypeChangeMulti').removeAttr("hidden")
            $('#systemCabMulti').prop("disabled", false)
            $('#typechangeMulti').prop("disabled", false)
            $('#divTypeChangeSingle').attr("hidden","hidden")
            $(".checkbox").attr("hidden","hidden")
            $("#link-hide-table").attr("hidden","hidden")
            $("#link-return-values").attr("hidden","hidden")
            $('.multiselect').val(null).trigger('change')
            $(".multiselect").prop("disabled", false)
            $('#save').prop('disabled', true)
            $("#stage1").prop("disabled", true)
            $("#stage21").prop("disabled", true)
            $("#stage22").prop("disabled", true)
            $("#stage23").prop("disabled", true)
            $("#stage3").prop("disabled", true)
            $("#authorize").prop("disabled", true)
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
            if($('.table-history').is(":visible"))  {
                $('.table-history').attr('hidden','');
                $("#link-hide-table").text("Показать историю")
            }
            $('.checkboxMulti').change()
        }   else    {
            $('.checkMultiDiv').attr("hidden","hidden")
            $('.description').attr("hidden","hidden")
            $("#active option[value='-1']").remove()
            $('#divSystemCabSingle').removeAttr("hidden")
            $('#divSystemCabMulti').attr("hidden","hidden")
            $('#divTypeChangeSingle').removeAttr("hidden")
            $('#divTypeChangeMulti').attr("hidden","hidden")
            $(".select").trigger("change")
            $(".checkbox").removeAttr("hidden")
            $(".multiselect").val("").trigger('change')
            $("#link-hide-table").removeAttr("hidden")
            $('#save').prop('disabled', false)
            $(".multiselect").prop("disabled", true)
        }
    })
})