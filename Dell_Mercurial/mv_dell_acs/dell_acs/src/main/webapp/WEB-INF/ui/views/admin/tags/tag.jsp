<script type="text/javascript">

    var Tagview = {
        ID:"",


        showTagPopup1:function (type, entityID) {

            $('#entityID').val(entityID);
            $.ajax({
                type: "GET",
                url: basePath + "admin/account/getTags.json",
                data: {entityID:entityID,type:type},
                success: function(response){
                    $('#tag').val(response);
                },
                error:function(e){
                    console.log('Error Detected',e);
                }
            });
            $('#item-tag-popup').show();
        },


        showTagPopup:function (tagValue) {


            if (tagValue != null || tagValue != "") {
                $('#item-tag-popup').show();
                $('#tags').append($('#tag').val());
            }

        },


        getTags:function (type, entityID) {



            $.ajax({
                type: "GET",
                url: basePath + "admin/account/getTags.json",
                data: {entityID:entityID,type:type},
                success: function(response){
                    $('#tag-'+entityID).append(response);
                },
                error:function(e){
                    console.log('Error Detected', e);
                }
            });

        },

        closePopup: function() {
            // Hide popup and empty the reviews list


            $('#item-tag-popup').hide();
        },

        cancelPopup: function() {
            // Hide popup and empty the reviews list
             $('#tags').empty();
             $('#tags').append($('#TagValue').val());
            $('#item-tag-popup').hide();
        },


        postSelectedTags:function (type, tagviewtype) {


            if (tagviewtype == "API_KEY") {

                var entityID = $('#entityID').val();
                $.ajax({
                    type:"POST",
                    url:basePath + "admin/account/tag.json",
                    data:{tag:$("#tag").val(), entityID:entityID, type:tagviewtype},
                    success:function (data) {
                        Tagview.closePopup();
                    },
                    error:function (e) {
                        console.log('Error occurred while tagging ', e);
                        location.reload();
                    }
                });
                location.reload();
            }
            else {

               $('#TagValue').val($('#tag').val());
                var trimmedString_tagVal;

                trimmedString_tagVal=Tagview.trimtext($('#tag').val());

                Tagview.closePopup();

            }
        },
        trimtext:function(tags_val)
        {

            var trimmedString_tagVal="";
            if(tags_val.length>100)
            {

                trimmedString_tagVal= tags_val.substr(0, 100);
                trimmedString_tagVal=trimmedString_tagVal+".....";
                $('#tags').empty();
                $('#tags').append(trimmedString_tagVal);

            }
            else
            {
                $('#tags').empty();
                $('#tags').append(tags_val);
            }
            return trimmedString_tagVal;

        },

        // Check reviews already selected for an Item with id = itemID

        checkForValueExists: function(valueString, reviewID) {
            if (valueString.length > 0) {
                var values = valueString.split(',');
                for (var idx = 0; idx < vals.length; idx++) {
                    if (values[i] == reviewID.toString()) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        }

    };



</script>
<style type="text/css">
    #tags_table{
        border: none;
        background: #fff;

        left: 35%;
    }
    .popup_header{
        background: #001422;
        color: #fff;
        padding: 10px;
    }
    .popup_actions{
        background: #ccc;
        float: none;
        overflow: hidden;
        padding: 10px;
        width: auto;
    }
    .popup_actions span {
        color: #fff;
        border: 1px solid #627d4d;
        height: 26px;
        line-height: 25px;
        padding: 0 10px;

        margin: 0;
        text-shadow: #444 1px 1px 0;

        background: #7ac142; /* Old browsers */
        /* IE9 SVG, needs conditional override of 'filter' to 'none' */
        background: url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/Pgo8c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgd2lkdGg9IjEwMCUiIGhlaWdodD0iMTAwJSIgdmlld0JveD0iMCAwIDEgMSIgcHJlc2VydmVBc3BlY3RSYXRpbz0ibm9uZSI+CiAgPGxpbmVhckdyYWRpZW50IGlkPSJncmFkLXVjZ2ctZ2VuZXJhdGVkIiBncmFkaWVudFVuaXRzPSJ1c2VyU3BhY2VPblVzZSIgeDE9IjAlIiB5MT0iMCUiIHgyPSIwJSIgeTI9IjEwMCUiPgogICAgPHN0b3Agb2Zmc2V0PSIwJSIgc3RvcC1jb2xvcj0iIzdhYzE0MiIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgICA8c3RvcCBvZmZzZXQ9IjEwMCUiIHN0b3AtY29sb3I9IiM3YWFhM2IiIHN0b3Atb3BhY2l0eT0iMSIvPgogIDwvbGluZWFyR3JhZGllbnQ+CiAgPHJlY3QgeD0iMCIgeT0iMCIgd2lkdGg9IjEiIGhlaWdodD0iMSIgZmlsbD0idXJsKCNncmFkLXVjZ2ctZ2VuZXJhdGVkKSIgLz4KPC9zdmc+);
        background: -moz-linear-gradient(top, #7ac142 0%, #7aaa3b 100%); /* FF3.6+ */
        background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #7ac142), color-stop(100%, #7aaa3b)); /* Chrome,Safari4+ */
        background: -webkit-linear-gradient(top, #7ac142 0%, #7aaa3b 100%); /* Chrome10+,Safari5.1+ */
        background: -o-linear-gradient(top, #7ac142 0%, #7aaa3b 100%); /* Opera 11.10+ */
        background: -ms-linear-gradient(top, #7ac142 0%, #7aaa3b 100%); /* IE10+ */
        background: linear-gradient(top, #7ac142 0%, #7aaa3b 100%); /* W3C */
        -moz-border-radius: 2px;
        -webkit-border-radius: 2px;
        border-radius: 2px;

        font-weight: bold;
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr = '#7ac142', endColorstr = '#7aaa3b', GradientType = 0); /*IE6-8*/
      }
    .popup_actions .close{
        margin-left: 10px;
    }
    #tag_box{
        overflow: hidden;
        height: auto;
        padding: 80px;
    }
    #tag_box span{
        font-weight: bold;
        padding-bottom: 10px;
        display: block;
    }
    #tag_box textarea{
             width: 100%;
    }

</style>
<div id="item-tag-popup" style="display: none;">
    <div class="pop_up_bg"></div>
    <div id="tags_table" class="tags_table">
        <div class="popup_header">
            <span class="pop_up_title">Tag View</span>

        </div>
        <div class="tag_popup_content" id="tag_popup_content">

            <div id="tag_box" class="tag_box">
                <span>Tag it</span>
                <textarea  name="tag" id="tag"></textarea>

               
                <input type="hidden" name="entityID" id="entityID"/>
            </div>
            <!-- Place holder for item reviews from server -->
            <table id="tags_list"></table>
        </div>
        <div class="popup_actions">
            <span class="close" onclick="Tagview.cancelPopup();">Close</span>
            <span onclick="Tagview.postSelectedTags($('#tag').val(),$('#Tagtype').val());">OK</span>

        </div>
    </div>
</div>
