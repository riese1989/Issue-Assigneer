$(function () {
    const typechange = document.getElementById("typechange")
    const system = document.getElementById("systemCab")
    document.querySelectorAll(".select").forEach(function (item) {
        item.addEventListener("change", (event) => {
            const stage1 = document.getElementById("stage1")
            const stage21 = document.getElementById("stage21")
            const stage22 = document.getElementById("stage22")
            const stage23 = document.getElementById("stage23")
            const stage3 = document.getElementById("stage3")
            const authorize = document.getElementById("authorize")
            const active = document.getElementById("active")
            if (typechange.value == "Select" || system.value == "Select") {
                stage1.setAttribute("disabled", "disabled")
                stage21.setAttribute("disabled", "disabled")
                stage22.setAttribute("disabled", "disabled")
                stage23.setAttribute("disabled", "disabled")
                stage3.setAttribute("disabled", "disabled")
                authorize.setAttribute("disabled", "disabled")
                active.setAttribute("disabled", "disabled")
            } else {
                stage1.removeAttribute("disabled")
                stage21.removeAttribute("disabled")
                stage22.removeAttribute("disabled")
                stage23.removeAttribute("disabled")
                stage3.removeAttribute("disabled")
                authorize.removeAttribute("disabled")
                active.removeAttribute("disabled")
            }
        })
    })
})
