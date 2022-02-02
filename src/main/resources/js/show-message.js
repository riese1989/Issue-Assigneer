$(function () {
    document.getElementById("save").addEventListener("click", function () {
        AJS.messages.success("#message", {
            body: '<p>Данные сохранены</p>'
        })
    });
})