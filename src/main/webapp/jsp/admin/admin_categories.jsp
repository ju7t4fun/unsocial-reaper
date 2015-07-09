<%--
  Created by IntelliJ IDEA.
  User: maryan
  Date: 29.06.2015
  Time: 17:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Creative - Bootstrap 3 Responsive Admin Template">
    <meta name="author" content="GeeksLabs">
    <meta name="keyword" content="Creative, Dashboard, Admin, Template, Theme, Bootstrap, Responsive, Retina, Minimal">

    <link rel="shortcut icon" href="${pageContext.request.contextPath}/img/icons/favicon.png">

    <title>All Posts</title>

    <!-- Bootstrap CSS -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <!-- bootstrap theme -->
    <link href="${pageContext.request.contextPath}/css/bootstrap-theme.css" rel="stylesheet">
    <!--external css-->
    <!-- font icon -->
    <link href="${pageContext.request.contextPath}/css/elegant-icons-style.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/font-awesome.min.css" rel="stylesheet"/>
    <!-- Custom styles -->
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style-responsive.css" rel="stylesheet"/>


    <!-- javascripts -->
    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <!-- nice scroll -->
    <script src="${pageContext.request.contextPath}/js/jquery.scrollTo.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.nicescroll.js" type="text/javascript"></script>
    <!-- gritter -->

    <%--<!-- custom gritter script for this page only-->--%>
    <script src="${pageContext.request.contextPath}/js/gritter.js" type="text/javascript"></script>
    <%--<!--custome script for all page-->--%>
    <script src="${pageContext.request.contextPath}/js/scripts.js"></script>
    <%--<script src="${pageContext.request.contextPath}/js/jquery.tokenize.js"></script>--%>

    <link href="${pageContext.request.contextPath}/css/fileinput.css" media="all" rel="stylesheet"
          type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/fileinput.min.js" type="text/javascript"></script>

    <%--for table--%>
    <script>
        var path = '${pageContext.request.contextPath}';
    </script>
    <link href="http://cdn.datatables.net/1.10.3/css/jquery.dataTables.css" rel="stylesheet" type="text/css">
    <link href="http://datatables.net/release-datatables/extensions/ColVis/css/dataTables.colVis.css" rel="stylesheet"
          type="text/css">
    <script src="http://cdn.datatables.net/1.10.3/js/jquery.dataTables.min.js"></script>
    <script src="http://datatables.net/release-datatables/extensions/ColVis/js/dataTables.colVis.js"></script>
    <script src="http://jquery-datatables-column-filter.googlecode.com/svn/trunk/media/js/jquery.dataTables.columnFilter.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/plugin/fnStandingRedraw.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/plugin/fnSetFilteringDelay.js"></script>

    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">


    <%--for tooltip--%>
    <script src="${pageContext.request.contextPath}/js/jquery-ui-1.9.2.custom.min.js"></script>


    <!-- HTML5 shim and Respond.js IE8 support of HTML5 -->
    <!--[if lt IE 9]>
    <script src="${pageContext.request.contextPath}/js/html5shiv.js"></script>
    <script src="${pageContext.request.contextPath}/js/respond.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/lte-ie7.js"></script>
    <![endif]-->

    <link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/toastr.js"></script>


    <script type="text/javascript">

        var table;


        jQuery(document).ready(function () {
            table = $('#categoryTable').dataTable({

                "bSort": true,
                aaSorting:[],
                "bPaginate": true,
                "paging": true,
                "bInfo": false,
                "iDisplayStart": 0,
                "bProcessing": true,
                'iDisplayLength': 10,
                "bServerSide": true,
                "sAjaxSource": path +  "/admin/categories?action=getcategory",
                colVis: {
                    "align": "right",
                    "buttonText": "columns <img src=\"/img/caaret.png\"/>",
                },

                "columnDefs": [

                    {
                        "targets": [0,1,2], "orderable": false
                    },

                    {
                        "aTargets": [1], "createdCell": function (td, cellData, rowData, row, col) {

                        var parts = cellData.split("|");


                        $(td).html('<a  href="#" title="" >'+parts[0]+'</a>')
                                .tooltip(
                                { content: '<img src="'+ parts[2]+'" width="300" height="200" width="300" />' },
                                { tooltipClass:"i1" } );

                    }
                    },

                    {
                        "aTargets": [2], "createdCell": function (td, cellData, rowData, row, col) {

                        $(td).html('<div class="btn-group"><a class="btn btn-danger" onclick="removeCategory('  + cellData + ')"><i class="icon_close_alt2"></i></a></div>');

                    }
                    }
                ]

            });

//            $(".dataTables_filter").attr("hidden", "");
            $(".dataTables_length").attr("hidden", "");
            var dataTables_filter_input = $(".dataTables_filter").find("input");
            dataTables_filter_input.attr("class", "form-control");
            dataTables_filter_input.attr("style", "width: 500px")

        })

        function hideElem(id) {
            document.getElementById(id).style.visibility = "hidden";
        }

        function showElem(id) {
            document.getElementById(id).style.visibility = "visible";
        }

        function addCat(name){

            var xmlhttp = new  XMLHttpRequest();
            xmlhttp.open("POST",path + "/admin/categories?action=addcategory",true);
            xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
            xmlhttp.send("category="+name);

            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4) {
                    table.fnStandingRedraw();
                }
            }
        }

        function removeCategory(i) {
            var xmlhttp = new  XMLHttpRequest();
            xmlhttp.open("POST", path +  "/admin/categories?action=removecategory",true);
            xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
            xmlhttp.send("id="+i);

            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4) {
                    table.fnStandingRedraw();
                }
            }
        }

    </script>

</head>
<body>

<!-- container section start -->
<section id="container" class="">

    <jsp:include page="../pagecontent/header.jsp"/>
    <jsp:include page="../pagecontent/sidebar.jsp"/>

    <%--<jsp:include page="../post/viewpost.jsp"/>--%>

    <section id="main-content">
        <section class="wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h3 class="page-header" style="width: 100%"><i class="fa fa-list-alt"></i>Categories</h3>

                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <div id="active" class="tab-pane active">
                                <div class="col-lg-12">

                                    <table width="100%" border="0" margin="0" padding="0"
                                           class="row-border tableHeader" id="categoryTable">
                                        <thead>
                                        <tr style="align-content: center">
                                            <th>id</th>
                                            <th>Category name</th>
                                            <th>Delete</th>
                                        </tr>
                                        </thead>
                                    </table>
                                    <a class="btn btn-primary" data-toggle="modal"
                                       data-target="#modal_category" href="#">Add category</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </section>
</section>


<div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="modal_category"
     class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content"  style="height: 520px; width: 820px">
            <div class="modal-header">
                <button aria-hidden="true" data-dismiss="modal" class="close" type="button">x</button>
                <h4 class="modal-title"  >Add category</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <form id="modal_form" method="POST"  action="" onsubmit="addCat(document.getElementById('catName').value)"
                              class="form-horizontal">
                            <div>
                                <input type="text" name="category" class="form-control" id="catName" placeholder="Category name">
                            </div>

                            <div id="compForm" class="container kv-main" style="width:800px;  margin-top:20px;">
                                <input id="input-dim-2" type="file"
                                       multiple="true" method="post"
                                       enctype="multipart/form-data" value=""
                                       accept="image/*">
                                <script>
                                    $("#input-dim-2").fileinput({
                                        uploadUrl: "/admin/categories?action=upCat",
                                        allowedFileExtensions: ['jpg', 'gif', 'png', 'jpeg'],
                                        maxFileCount: 1
                                    });
                                </script>
                            </div>

                            <div style="position: absolute; top: 420px;right: 2% ">
                                <button type="submit" class="btn btn-primary" >Add</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<style>
    .i1   {
        position: fixed;
        background: red;
        font-size:12px;
        height:250px;
        width:350px;
        padding:20px;
        color:#fff;
        z-index: 99;
        border: 2px solid white;

    }
</style>
</body>
</html>

