var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
var jiraURL = currentURL.split("secure")[0]
const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/'
$.get(jiraRestAddress + 'getactiveusers', function (response) {
    const stages = ["#stage1", "#stage21", "#stage22", "#stage23", "#stage3", "#authorize", "#delivery"]
    var parser = new DOMParser();
    var doc = parser.parseFromString(response, 'text/xml');
    var rootElement = doc.documentElement;
    var children = rootElement.childNodes;
    for(var i =0; i< children.length; i++) {
        var child = children[i]
        if (child.nodeType == Node.ELEMENT_NODE)    {
            var nameElem = child.getElementsByTagName("id")[0]
            var idElem = child.getElementsByTagName("name")[0]
            var name = nameElem.textContent
            var id = idElem.textContent
            $.each(stages, function (idStage, stage) {
                var newOption = document.createElement("option")
                newOption.innerHTML = id
                newOption.value = name
                document.querySelector(stage).append(newOption)
            })
        }
    }
})
