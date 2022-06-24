$(function () {
    document.getElementById("save").addEventListener("click", function () {
        const message = $('#message')
        message.show()
        AJS.messages.success("#message", {
            body: '<p>Данные сохранены</p>',
        })
        message.delay(3000).fadeOut('slow');
    });
})