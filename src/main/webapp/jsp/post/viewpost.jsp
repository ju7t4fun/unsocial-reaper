<%--<script type='text/javascript' src='${pageContext.request.contextPath}/js/jquery-11.0.min.js'></script>--%>
<%@ taglib prefix="l" uri="http://lab.epam.com/spider/locale" %>
<script type='text/javascript' src='${pageContext.request.contextPath}/js/unitegallery.min.js'></script>

<link rel='stylesheet' href='${pageContext.request.contextPath}/css/unite-gallery.css' type='text/css'/>

<script type='text/javascript' src='${pageContext.request.contextPath}/js/ug-theme-default.js'></script>
<link rel='stylesheet' href='${pageContext.request.contextPath}/css/ug-theme-default.css' type='text/css'/>

<div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content" style="width: 840px; position: relative; left: -100px;">
            <div class="modal-header">
                <button aria-hidden="true" data-dismiss="modal" class="close" type="button">x</button>
                <h3 class="modal-title"><l:resource key="post"/></h3>
            </div>
            <div class="modal-body" style="position: relative; left: 70px; top: -21px;">
                <div class="row">
                    <div class="col-md-8 portlets">
                        <div class="">
                            <div class="panel-body" style="width: 700px; margin-left: -15px;">
                                <ul style="margin-left:-30px;">
                                    <li>
                                        <tr>
                                            <td style="text-align:justify;">
                                                <span id="post_text"></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <div id="gallery" style="display:none; margin-top: 20px;">
                                            </div>
                                        </tr>
                                    </li>
                                    <hr>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function viewPost(id) {
        $.get(
                "${pageContext.request.contextPath}/post?action=getPostById&post_id="+id,
                onAjaxSuccess
        );
        function onAjaxSuccess(data) {
            var response = data;
            $("#post_text").html(response.postText);
            $("#gallery").empty();
            if (response.attachments.length != 0) {
                var div_gallery = $("#gallery");
                for (var i = 0; i < response.attachments.length; i++) {
                    var image = $('<img>');
                    switch (response.attachments[i].type) {
                        case "photo":
                            image.attr("src", response.attachments[i].url);
                            image.attr("data-image", response.attachments[i].url);
                            break;
                        case "youtube":
                            image.attr("data-type", "youtube");
                            image.attr("data-videoid", response.attachments[i].url);
                            break;
                        case "vk_video":
                            image.attr("src", "${pageContext.request.contextPath}/img/poster.jpg");
                            image.attr("data-type", "VKONTAKTE");
                            image.attr("data-image", "${pageContext.request.contextPath}/img/poster.jpg");
                            image.attr("data-videoid", response.attachments[i].url);
                            break;
                        case "audio":
                            image.attr("src", "${pageContext.request.contextPath}/img/poster.jpg");
                            image.attr("data-type", "html5video");
                            image.attr("data-image", "${pageContext.request.contextPath}/img/poster.jpg");
                            image.attr("data-videomp4", response.attachments[i].url);
                            break;
                        case "video":
                            image.attr("src", "${pageContext.request.contextPath}/img/poster.jpg");
                            image.attr("data-type", "html5video");
                            image.attr("data-image", "${pageContext.request.contextPath}/img/poster.jpg");
                            image.attr("data-videomp4", response.attachments[i].url);
                            break;
                    }
                    div_gallery.append(image);
                }
                initGallery();
            }
        }
    }
</script>
<script type="text/javascript">
    function initGallery() {
        jQuery("#gallery").unitegallery({

            theme_enable_fullscreen_button: true,
            //show, hide the theme fullscreen button. The position in the theme is constant
            theme_enable_play_button: true,			//show, hide the theme play button. The position in the theme is constant
            theme_enable_hidepanel_button: true,	//show, hide the hidepanel button
            theme_enable_text_panel: true,			//enable the panel text panel.

            theme_text_padding_left: 20,			//left padding of the text in the textpanel
            theme_text_padding_right: 5,			//right paddin of the text in the textpanel
            theme_text_align: "left",				//left, center, right - the align of the text in the textpanel
            theme_text_type: "title",				//title, description - text that will be shown on the text panel, title or description

            theme_hide_panel_under_width: 480		//hide panel under certain browser width, if null, don't hide
        });
    }
</script>