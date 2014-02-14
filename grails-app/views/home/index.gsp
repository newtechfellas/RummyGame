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
    <r:require modules="jquery, spring-websocket"/>
    <r:script>
            var socket = new SockJS("${createLink(uri: '/stomp')}");
            var client = Stomp.over(socket);

            client.connect({}, function() {
                client.subscribe("/topic/loggedIn", function(message) {
                    var userName = message.body.replace(/"/g,'') //Not sure why extra double quotes surround the message body string
                    var activeStatus = $("td.chat_player_id:contains('"+userName+"')").next()
                    if ( activeStatus && activeStatus.text().trim() == 'INACTIVE') {
                        activeStatus.html('ACTIVE')
                    }
                });
                client.subscribe("/topic/loggedOut", function(message) {
                    var userName = message.body.replace(/"/g,'') //Not sure why extra double quotes surround the message body string
                    var activeStatus = $("td.chat_player_id:contains('"+userName+"')").next()
                    if ( activeStatus && activeStatus.text().trim() == 'ACTIVE') {
                        activeStatus.html('INACTIVE')
                    }
                });
            });
    </r:script>
</head>

<body>
<div class="container">

    <div class="section">
        <h1>Welcome, ${session.userName} <g:link class="btn btn-warning" uri="/logout">Log out</g:link></h1>
    </div>
    <g:hasErrors bean="${command}">
        <div class="errors">
            <g:renderErrors as="list" bean="${command}"/>
        </div>
    </g:hasErrors>

    <div class="row">
        <div class="col-md-6">
            <div class="well">
                <!-- Start New Game Option -->
                <h4>Start New Game</h4>
                <g:form action="startNewGame">
                    <section id="NewGameSection">
                        <div id="emailIds">
                            <fieldset class="form">
                                <g:textArea name="playerIds" title="Enter Player Mail Ids One Per Row"
                                            placeholder="Enter Player EmailIds One Per Row" cols="70"
                                            rows="5"></g:textArea>
                                <br/>
                                <br>
                                Game Name <g:textField name="gameName"></g:textField>
                            </fieldset>
                        </div>
                        <g:submitButton value="Create New Game And Send Invite" name="Create New Game And Send Invite"/>
                    </section>
                </g:form>
            </div>
        </div>
    <!-- Existing Games open  -->
        <g:if test="${openGamesByMeJson || openInvitedGamesJson }">
            <div class="col-md-6">
                <div class="well">
                    <div id="existingGames" class="yui-skin-sam">
                        <h4>Games History</h4>
                        <!-- YUI Table to hold the existing games data -->
                        <div id="existingStartedByMeTable"></div>

                        <!-- YUI Table to hold the existing games data -->
                        <div id="invitedGamesTable"></div>
                    </div>
                </div>
            </div>
        </g:if>


        <div class="col-md-6">
            <div class="well">
                <h4>Friends List</h4>
                <table class="table table-striped">
                    <tbody>
                    <g:each in="${currentPlayerFriends}" var="player">
                        <tr>
                            <td class="chat_player_id">
                                ${player.emailId}
                            </td>
                            <td>
                                <g:if test="${player.isLoggedInNow}">
                                    ACTIVE
                                </g:if>
                                <g:else>
                                    INACTIVE
                                </g:else>
                            </td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div><!-- /.container -->
<script>
    <g:if test="${openGamesByMeJson}">
    function drawExistingGamesStartedByMeTable(openGamesByMeJson) {
        var deleteCellFormatter = function (elCell, oRecord, oColumn, oData) {
            elCell.innerHTML = '<a title="Delete Game" href="javascript:void(0);"><img class="yuiCellImage" alt="Delete Game" src="https://raw2.github.com/iWebi/RummyGame/master/WebContent/images/24_empty_trash.jpg"></a>';
        };

        var idCellFormatter = function (elCell, oRecord, oColumn, oData) {
            elCell.innerHTML = '<a title="id" href="/games/rummy/game?id=' + oData + '">' + oData + '</a>';
        };


        var myColumnDefs = [
            {key: "gameName", label: "Name", formatter: idCellFormatter, sortable: true, resizeable: true},
            {key: "creationTime", label: "Created On", sortable: true, resizeable: true},
            {key: "lastUpdatedTime", label: "Last Modified On", sortable: true, resizeable: true},
            {key: "delete", label: "", formatter: deleteCellFormatter, sortable: true, resizeable: true},
        ];

        var myDataSource = new YAHOO.util.DataSource(openGamesByMeJson);
        myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;

        myDataSource.responseSchema = { fields: ["creationTime", "id", "gameName", "lastUpdatedTime" ] };
        var myDataTable = new YAHOO.widget.DataTable("existingStartedByMeTable", myColumnDefs, myDataSource,
                {
                    caption: "Games Started By Me",
                    paginator: new YAHOO.widget.Paginator({
                        rowsPerPage: 6,
                        alwaysVisible: false
                    })
                });

        //Delete Game event handling
        myDataTable.subscribe('cellClickEvent', function (ev) {
            var target = YAHOO.util.Event.getTarget(ev);
            var column = myDataTable.getColumn(target);

            if (column.key == 'delete') {
                var id = this.getRecord(target).getData('id');
                var deleteConfirm = window.confirm("This can not be undone. Are you sure you want to delete the game?")
                if (deleteConfirm) {
                    myDataTable.deleteRow(target);
                }
            }
        });
    }
    drawExistingGamesStartedByMeTable(${openGamesByMeJson});
    </g:if>

    <g:if test="${openInvitedGamesJson}">
    function drawInvitedGamesTable(openInvitedGamesJson)
    {
        var gameIdCellFormatter = function(elCell, oRecord, oColumn, oData) {
            elCell.innerHTML = '<a title="GameId" href="/games/rummy/game?gameId='+oData+'">'+oData+'</a>';
        };

        var myColumnDefs = [
            {key:"gameName", label: "Name", formatter:gameIdCellFormatter, sortable:true,resizeable:true},
            {key:"originatorPlayerID", label: "InvitedBy", sortable:true,resizeable:true},
            {key:"creationTime", label: "Created On", sortable:true, resizeable:true},
            {key:"lastUpdatedTime", label:"Last Modified On", sortable:true,resizeable:true},
            {key:"action", label:"Action", formatter:"dropdown", dropdownOptions:["-Select-","Accept","Reject","Delete"],resizeable:true},
        ];

        var myDataSource = new YAHOO.util.DataSource(openInvitedGamesJson);
        myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;

        myDataSource.responseSchema = { fields: ["creationTime","gameId", "originatorPlayerID", "gameName", "lastUpdatedTime", "action"] };
        var myDataTable = new YAHOO.widget.DataTable("invitedGamesTable", myColumnDefs, myDataSource,
                {
                    caption:"Games I Got Invited For",
                    paginator: new YAHOO.widget.Paginator({
                        rowsPerPage: 6,
                        alwaysVisible : false
                    })
                });
        myDataTable.subscribe("dropdownChangeEvent", function(oArgs){
            var elDropdown = oArgs.target;
            var oRecord = this.getRecord(elDropdown);
            oRecord.setData("action",elDropdown.options[elDropdown.selectedIndex].value);
            alert("hehehehe. This feature is not yet built. Hang on till next upgrade!!!");
        });
    }
    drawInvitedGamesTable(${openInvitedGamesJson});
    </g:if>
</script>

</body>
</html>
