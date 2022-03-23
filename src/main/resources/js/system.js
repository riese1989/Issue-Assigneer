$(function () {
    var j = 1
    $(document.body).on("change", ".checkbox", function () {
        j++
        if (j%3 === 0) {
            var systemCab = document.getElementById("systemCab");
            while (systemCab.length !== 0) {
                for (let i = 0; i <= systemCab.length - 1; i++) {
                    systemCab.remove(i)
                }
            }
            const jiraAddress = 'http://localhost:2990/jira/'
            var markedCheckbox = document.getElementsByName("checkbox")
            var values = ""
            for (var checkbox of markedCheckbox) {
                if (checkbox.checked)
                    values += checkbox.value + ','
            }
            var newOption = document.createElement("option");
            newOption.innerHTML = "Select"
            newOption.value = "0";
            document.querySelector('#systemCab').append(newOption)
            var param = values.replaceAll("function values() { [native code] }", "")
            var restAddress = jiraAddress + 'rest/cab/1.0/systems/getlistsystems?valuefilter=' + param;
            $.get(restAddress, function (response) {
                let hashMapSystems = response.substr(1, response.length - 2);
                if (hashMapSystems !== "") {
                    let mapSystems = hashMapSystems.split(", ")
                    mapSystems.forEach((system) => {
                        newOption = document.createElement("option");
                        let dataSystem = system.split("=");
                        newOption.innerHTML = dataSystem[1]
                        newOption.value = dataSystem[0];
                        document.querySelector('#systemCab').append(newOption)
                    })
                }
            })
            j = 0
            for (checkbox of markedCheckbox) {
                if (checkbox.checked && j !== 0 && systemCab.length !== 0)    {
                    systemCab.selectedIndex = 0;
                    $('#systemCab').trigger('change')
                    break
                }
                j++
            }
        }
    })
})

