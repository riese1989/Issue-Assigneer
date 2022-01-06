$(function () {
    const typechange = document.getElementById("typechange")
    const system = document.getElementById("systemCab")
    document.querySelectorAll(".select").forEach(function (item) {
        item.addEventListener("change", (event) => {
            const step1 = document.getElementById("step1")
            const step21 = document.getElementById("step21")
            const step22 = document.getElementById("step22")
            const step23 = document.getElementById("step23")
            const step3 = document.getElementById("step3")
            const autorize = document.getElementById("autorize")
            const active = document.getElementById("active")
            if (typechange.value == "Select" || system.value == "Select") {
                step1.setAttribute("disabled", "disabled")
                step21.setAttribute("disabled", "disabled")
                step22.setAttribute("disabled", "disabled")
                step23.setAttribute("disabled", "disabled")
                step3.setAttribute("disabled", "disabled")
                autorize.setAttribute("disabled", "disabled")
                active.setAttribute("disabled", "disabled")
            } else {
                step1.removeAttribute("disabled")
                step21.removeAttribute("disabled")
                step22.removeAttribute("disabled")
                step23.removeAttribute("disabled")
                step3.removeAttribute("disabled")
                autorize.removeAttribute("disabled")
                active.removeAttribute("disabled")
            }
        })
    })
})
