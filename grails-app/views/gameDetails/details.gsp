<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="layout" content="main">

    <title>Welcome</title>

    <!-- Bootstrap core CSS -->
    <g:external uri="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css"/>
    <style>
    body {
        padding-top: 50px;
    }

    .section {
        padding: 40px 15px;
        text-align: center;
    }

    .message {
        font: bold;
    }

    .chat_player {
    }
    </style>
    <link rel="stylesheet" href="http://yui.yahooapis.com/2.9.0/build/fonts/fonts-min.css"/>
    <link rel="stylesheet" href="http://yui.yahooapis.com/2.9.0/build/datatable/assets/skins/sam/datatable.css"/>
    <link rel="stylesheet" type="text/css"
          href="http://yui.yahooapis.com/2.6.0/build/button/assets/skins/sam/button.css"/>
    <link rel="stylesheet" type="text/css"
          href="http://yui.yahooapis.com/2.9.0/build/paginator/assets/skins/sam/paginator.css"/>

    <!-- Combo-handled YUI CSS files: -->
    <link rel="stylesheet" type="text/css"
          href="http://yui.yahooapis.com/combo?2.9.0/build/paginator/assets/skins/sam/paginator.css&2.9.0/build/datatable/assets/skins/sam/datatable.css">
    <!-- Combo-handled YUI JS files: -->
    <script type="text/javascript"
            src="http://yui.yahooapis.com/combo?2.9.0/build/yahoo-dom-event/yahoo-dom-event.js&2.9.0/build/connection/connection-min.js&2.9.0/build/element/element-min.js&2.9.0/build/paginator/paginator-min.js&2.9.0/build/datasource/datasource-min.js&2.9.0/build/datatable/datatable-min.js&2.9.0/build/json/json-min.js"></script>

    <script src="http://yui.yahooapis.com/3.8.1/build/yui/yui-min.js"></script>
    <script src="http://yui.yahooapis.com/2.9.0/build/event-delegate/event-delegate-min.js"></script>
    <script src="http://yui.yahooapis.com/2.9.0/build/dom/dom-min.js"></script>
    <script src="http://yui.yahooapis.com/2.9.0/build/button/button.js"></script>
    <script src="http://yui.yahooapis.com/2.9.0/build/json/json-min.js"></script>

    <style type="text/css">
    .yui-skin-sam .yui-dt-liner {
        white-space: nowrap;
    }

    .yuiCellImage {
        height: 25px;
    }

    #paginated {
        text-align: center;
    }

    #paginated table {
        margin-left: auto;
        margin-right: auto;
    }

    #paginated, #paginated .yui-dt-loading {
        text-align: center;
        background-color: transparent;
    }
    </style>
</head>
<body>
    <div class="section">
        <h1>Welcome, ${session.userName} <g:link class="btn btn-warning" uri="/logout">Log out</g:link></h1>
    </div>
    <g:if test="${message}">
        <h1>${message}</h1>
    </g:if>
    <g:else>Game exists</g:else>
</body>
</html>
