$(function () {
    var j = 1
    var countCheckedBefore = 0;
    $(document.body).on("change", ".checkbox", function () {
        j++
        if (j % 2 === 0) {
            var systemCab = document.getElementById("systemCab");
            while (systemCab.length !== 0) {
                for (var i = 0; i <= systemCab.length - 1; i++) {
                    systemCab.remove(i)
                }
            }
            var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
            var jiraURL = currentURL.split("secure")[0]
            const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
            var markedCheckbox = document.getElementsByName("checkbox")
            var values = ""
            for (let checkbox of markedCheckbox) {
                if (checkbox.checked)
                    values += checkbox.value + ','
            }
            if (values === "") {
                $('#save').prop('disabled', true)
            } else {
                var newOption = document.createElement("option")
                newOption.innerHTML = "Select"
                newOption.value = "0";
                document.querySelector('#systemCab').append(newOption)
                var param = values.replaceAll("function values() { [native code] }", "")
                $.get(jiraRestAddress + 'getlistsystems?valuefilter=' + param, function (response) {
                    var hashMapSystems = response.substr(1, response.length - 2);
                    if (hashMapSystems !== "") {
                        var mapSystems = hashMapSystems.split(", ")
                        mapSystems.forEach((system) => {
                            newOption = document.createElement("option")
                            let dataSystem = system.split("=")
                            newOption.innerHTML = dataSystem[1]
                            newOption.value = dataSystem[0];
                            document.querySelector('#systemCab').append(newOption)
                        })
                    }
                })
                var countCheckedAfter = 0;
                for (let checkbox of markedCheckbox) {
                    if (checkbox.checked)
                        countCheckedAfter++
                }
                if (countCheckedBefore !== countCheckedAfter || countCheckedAfter === 0)    {
                    var typeChange = document.getElementById("typechange")
                    typeChange.selectedIndex = 0
                    systemCab.selectedIndex = 0
                    $('.select').trigger('change')
                }
                countCheckedBefore = countCheckedAfter
            }
        }
    })
})

