$(function () {
    $(document.body).on("change", "#systemCabMulti", function () {
        var jiraURL = $(location).attr("href").split("secure")[0]
        var jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        var textPart1 = "У выбранных систем в поле "
        var textPart2 = " разные значения. Выберите какое-то одно, иначе значения в этом поле не сохранятся"
        var elemDelivery = $("#deliveryLabel")
        var elemActive = $("#activeLabel")
        $.get( jiraRestAddress + "getcad?idSystems=" + $('#systemCabMulti').val(), function (response)    {
            var obj = jQuery.parseJSON(response);
            if (obj.countDelivery > 1)  {
                $('#descriptionDelivery').removeAttr("hidden")
                $('#descriptionDelivery').text(textPart1 + elemDelivery.text() + textPart2)
            }   else    {
                $('#descriptionDelivery').attr("hidden","hidden")
            }
            if (obj.countActive > 1)    {
                $('#descriptionActive').removeAttr("hidden")
                $('#descriptionActive').text(textPart1 + elemActive.text() + textPart2)
            }   else    {
                $('#descriptionActive').attr("hidden","hidden")
            }
        });

    })
})