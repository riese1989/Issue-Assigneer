var jiraURL = $(location).attr("href").split("secure")[0]
var jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
var json = {
    systems: [1,2,76,95],
    typechanges: [1,2,3],
    stage1: {
        ids:[900, 567, 344],
        enable: true
    },
    stage21: {
        ids:[900, 567, 344],
        enable: false
    },
    delivery:16,
    active:true
}
console.log(json)
$.post( jiraRestAddress + "postmulti", json);


const system = document.getElementById("systemCabMulti")
console.log(system.value)

$("#active").val();

$('#systemCabMulti option:selected').val()

$('#systemCabMulti').val()

var system = document.getElementById("systemCab")
console.log(system.value)
console.log(system.value !== '0')