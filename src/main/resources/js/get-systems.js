$(document).ready(function() {
    $('.checkboxes').change(function (){
        $('#systemCab').empty()
        $('#systemCab').append(new Option("Select", "0"))
        var values = ""
        $(':checkbox').each(function() {
            if (this.checked)   {
                values += this.value + ","
            }
        })
        var jiraURL = $(location).attr("href").split("secure")[0]
        var jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
        if (values !== "")  {
            $.get(jiraRestAddress + 'getlistsystems?valuefilter=' + values, function (response) {
                var hashMapSystems = response.substr(1, response.length - 2) + ""
                if (hashMapSystems !== "") {
                    var systems = hashMapSystems.split(", ")
                    $(systems).each(function (index, system){
                        console.log(system)
                        var dataSystem = system.toString().replace("&&&&&&&&&&&&&&&&&&",", ").split("=")
                        $('#systemCab').append(new Option(dataSystem[1], dataSystem[0]))
                    })
                }

            })
        }
        $('.select').val('0').change()
    })
})
