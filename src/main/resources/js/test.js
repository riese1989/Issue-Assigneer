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
            console.log(jiraRestAddress + 'getlistsystems?valuefilter=' + values)
            $.get(jiraRestAddress + 'getlistsystems?valuefilter=' + values, function (response) {
            })
        }
    })
})
