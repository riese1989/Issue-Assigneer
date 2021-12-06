AJS.$(document).ready
(
    function()
    {
        createMultiSelectField();
        JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED,function(b,a)
            {
                createMultiSelectField()}
        )
    }
);

function createMultiSelectField()
{
    AJS.$(".select2-example").not("[select2='true']").each(
        function(a)
        {
            new AJS.MultiSelect
            (
                {
                    element:this,
                    itemAttrDisplayed:"label",
                    errorMessage:AJS.params.multiselectComponentsError,
                    showDropdownButton:true
                }
            );
            AJS.$(this).attr("select2","true")
        }
    )
}
