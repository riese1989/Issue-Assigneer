$(function () {
    let j = 1
    let countCheckedBefore = 0
    $(document.body).on("change", ".checkbox", function () {
        console.log("Change checkbox")
        while (j <= 2) {
            if (j % 2 === 0) {
                const systemCab = document.getElementById("systemCab")
                const typeChange = document.getElementById("typechange")
                while (systemCab.length !== 0) {
                    for (let i = 0; i <= systemCab.length - 1; i++) {
                        systemCab.remove(i)
                    }
                }
                let newOption = document.createElement("option")
                newOption.innerHTML = "Select"
                newOption.value = "0"
                document.querySelector('#systemCab').append(newOption)
                let currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
                let jiraURL = currentURL.split("secure")[0]
                const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
                const markedCheckbox = document.getElementsByName("checkbox")
                let values = ""
                for (let checkbox of markedCheckbox) {
                    if (checkbox.checked)
                        values += checkbox.value + ','
                }
                if (values !== "") {
                    let param = values.replaceAll("function values() { [native code] }", "")
                    $.get(jiraRestAddress + 'getlistsystems?valuefilter=' + param, function (response) {
                        console.log(jiraRestAddress + 'getlistsystems?valuefilter=' + param)
                        let hashMapSystems = response.substr(1, response.length - 2)
                        if (hashMapSystems !== "") {
                            let mapSystems = hashMapSystems.split(", ")
                            mapSystems.forEach((system) => {
                                newOption = document.createElement("option")
                                let dataSystem = system.split("=")
                                newOption.innerHTML = dataSystem[1]
                                newOption.value = dataSystem[0]
                                document.querySelector('#systemCab').append(newOption)
                            })
                        }
                    })
                }
                let countCheckedAfter = 0
                for (let checkbox of markedCheckbox) {
                    if (checkbox.checked)
                        countCheckedAfter++
                }
                if (countCheckedBefore !== countCheckedAfter || countCheckedAfter === 0 || values === "") {
                    typeChange.selectedIndex = 0
                    systemCab.selectedIndex = 0
                    $('.select').trigger('change')
                }
                countCheckedBefore = countCheckedAfter
            }
            j++
        }
    })
})