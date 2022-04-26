$(document).ready(function() {
    $("#link-hide-table").text("Показать историю")
    $("#link-hide-table").click(function(){
        if($('.table-history').is(":visible"))  {
            $('.table-history').attr('hidden','');
            $("#link-hide-table").text("Показать историю")
        }   else    {
            $('.table-history').removeAttr("hidden")
            $("#link-hide-table").text("Скрыть историю")
        }
    })
})
