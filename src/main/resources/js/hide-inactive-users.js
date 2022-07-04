$(function () {
    $(document.body).on("change", ".select", function () {
        setTimeout(getInactiveUsers, 200)
    })
})

function getInactiveUsers()  {
    const stages = ["#stage1", "#stage21", "#stage22", "#stage23", "#stage3", "#authorize", "#delivery"]
    $.each(stages, function (idStage, stage) {
        $(stage + " > option").each(function() {
            if (this.text.includes(["[X]"]))    {
                $(stage + " option[value='" + this.value +"']").wrap("<span/>")
            }
        });
    })
}