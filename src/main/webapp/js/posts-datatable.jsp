<%@page contentType="text/javascript" %>
var table;


function reloadData(postId) {

    document.getElementById("showAllBtnId").style.visibility = "visible";
    table.api().ajax.url("${pageContext.request.contextPath}/post?action=getPosted&postId=" + postId).load();

    document.getElementById("showAllBtnId").style.visibility = "visible";
}

        jQuery(document).ready(function () {

    table = $('#postsTable').dataTable({

        "bPaginate": true,
        aaSorting: [],
        paging: true,
        "bInfo": false,
        "iDisplayStart": 0,
        "bProcessing": true,
        'iDisplayLength': 10,
        "bServerSide": true,
        "sAjaxSource": "${pageContext.request.contextPath}/post?action=getPosted",
        colVis: {
            "align": "right",
            "buttonText": "columns <img src=\"${pageContext.request.contextPath}/img/caaret.png\"/>",
        },
        //showAllBtnId
        "sDom": '<"top"<"toolbar">f>t<"bottom"lp><"clear">',

        "aoColumnDefs": [{
            "targets": [0, 1, 2, 4, 5, 6], "orderable": false
        }, {
            "class": "dt-body-center", "targets": [4, 6]
        }, {
            "class": "dt-body-left", "targets": 0
        }, {
            "width": "50%", "targets": 0
        }, {
            "width": "10%", "targets": 3
        }, {
            "class": "dt-body-left", "targets": 4
        }, {
            "bVisible": false, "aTargets": [5]
        }, {
            "aTargets": [0], "createdCell": function (td, cellData, rowData, row, col) {
                $(td).html('<a target="_blank" href="http://vk.com/wall' + rowData[2] + '" >' + cellData + '</a>');
            }
        }, {
            "aTargets": [1], "createdCell": function (td, cellData, rowData, row, col) {
                var strCellValue = '<a href=\"javascript:reloadData(\'' + rowData[6] + '\')\" >' + cellData + "</a>";
                $(td).html(strCellValue);
            }
        }, {
            "aTargets": [4], "createdCell": function (td, cellData, rowData, row, col) {
                var tokens = cellData.split("|");
                $(td).html('<table onclick="openPostStats(\'' + rowData[6] + '\')"><tr>' +
                    '<td><i style="color: #6c6c6c;" class="fa fa-heart"></i> <span class="badge bg-important">' + tokens[0] + '</span></td>' +
                    '<td><i style="color: #6c6c6c;" class="fa fa-bullhorn"></i> <span class="badge bg-important">' + tokens[1] + '</span></td>' +
                    '<td><i style="color: #6c6c6c;" class="fa fa-comment"></i> <span class="badge bg-important">' + tokens[2] + '</span></td>' +
                    '</tr></table>');
            }
        }, {
            "width": "30%", "targets": 4
        }, {
            "width": "5%", "targets": 6
        }, {
            "bVisible": false, "aTargets": [2]
        }, {
            "aTargets": [6], "createdCell": function (td, cellData, rowData, row, col) {
                $(td).html('<a class="btn btn-danger" onclick="removePost(' + cellData + ')"><i' +
                    ' class="icon_close_alt2"></i></a>');
            }
        }]

    });

    //$(".dataTables_filter").attr("hidden", "");
    //$(".dataTables_length").attr("hidden", "");
    var dataTables_filter_input = $(".dataTables_filter").find("input");
    dataTables_filter_input.attr("class", "form-control");
    dataTables_filter_input.attr("style", "width: 500px");

    $("div.toolbar").html('<input style="position: absolute; top: 35px; left: 35px; visibility: hidden" class="btn btn-default" type="button" id="showAllBtnId" onclick="myFunc()" value="Show All"/>');

    $('#refreshbtn').click(function () {
        table.fnStandingRedraw();
    });

})
;

function removePost(i) {
    deleteConfirmPosted(i);
}

        function parseAttachment(arg) {
    var args = arg.split("!");
    var cell = "";
    for (var i = 0; i < args.length; i++) {
        cell = cell + " " + parseDoc(args[i]);
    }
    return cell;
}

        function parseDoc(arg) {
    var args = arg.split("|");
    switch (args[0]) {
        case "PHOTO":
            return '<img src=\"${pageContext.request.contextPath}/img/icons/jpg-icon.png" style="width: 30px; height: 30px"><span class="badge bg-important">{count}</span>'.replace("{count}", args[1]);
        case "VIDEO":
            return '<img src=\"${pageContext.request.contextPath}/img/icons/mpg-icon.png" style="width: 30px; height: 30px"><span class="badge bg-important">{count}</span>'.replace("{count}", args[1]);
        case "AUDIO":
            return '<img src=\"${pageContext.request.contextPath}/img/icons/mp3-icon.png" style="width: 30px; height: 30px"><span class="badge bg-important">{count}</span>'.replace("{count}", args[1]);
        case "DOC":
            return '<img src=\"${pageContext.request.contextPath}/img/icons/txt-icon.png" style="width: 30px; height: 30px"><span class="badge bg-important">{count}</span>'.replace("{count}", args[1]);
    }
    return "";
}
