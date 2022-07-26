$(function () {
    document.getElementById("save").addEventListener("click", function () {
        var toggle = document.getElementById('bulk-edit')
        if (toggle.checked === true)    {
            const nameElems = ["stage1", "stage21", "stage22", "stage23", "stage3", "authorize", "delivery", "active"]
            var currentURL = window.location.protocol + "//" + window.location.host + window.location.pathname + window.location.search
            var jiraURL = currentURL.split("secure")[0]
            const jiraRestAddress = jiraURL + 'rest/cab/1.0/systems/postmulti'
            var json = []
            var item = {}
            item['systems'] = $('#systemCabMulti').val()
            item['typechanges'] = $('#typechangeMulti').val()
            $.each(nameElems, function (i, n) {
                var elem = $("#" + n)
                var jsonNested = []
                var itemNested = {}
                itemNested['ids'] = $(n).val()
                itemNested['enable'] = elem.is(':disabled')
                jsonNested.push(itemNested)
                item[n] = jsonNested
            })
            json.push(item)
            console.log(json)
            $.post( jiraRestAddress, json);
        }
    });
})