<%@ page import="com.webi.games.rummy.entity.RummyGame" %>
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
    .ngrt {
        font: bold;
    }
    </style>
    <!-- Combo-handled YUI JS files: -->
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
    <r:require modules="jquery, spring-websocket"/>
    <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
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
                client.subscribe("/topic/general", function(message) {
                    var json = JSON.parse(JSON.parse(message.body));
                    for (var i=0 ; i < json.messages.length;i++)
                    {
                        var message = json.messages[i]
                        switch (message.type) {
                            case 'ngrt' :
                               if ( $('#'+'gameStartedByMe_'+message.gameId).exists() ) { //if the game is startedBy current logged in player display the message to him
                                $('#notifcations').append('<p class="ngrt">'+message.fromUser+ ' accepted '+message.gameName+' game</p>')
                               }
                               var openInviteRowElement = $('#'+'openInvite_row_'+message.gameName+'_'+message.gameId);
                               if (openInviteRowElement.exists()) {
                                    //grab its first 2 cells and use for participatedGamesTable
                                   $(openInviteRowElement).find('td:last').replaceWith('<td>WON</td>');
                                    $('#participatedGames table tbody').append(openInviteRowElement);
                                    $('#participatedGames').show()
                               }
                        }
                    }
                });
            });
    </r:script>
    <script type="text/javascript">
        $(document).ready( function(){
            $(".cb-enable").click(function(){
                sendNewGameResponse($(this).attr('gameId'), $(this).attr('gameName'), 'Accept')

                var parent = $(this).parents('.switch');
                $('.cb-disable',parent).removeClass('selected');
                $(this).addClass('selected');
                $('.checkbox',parent).attr('checked', true);
            });
            $(".cb-disable").click(function(){
                sendNewGameResponse($(this).attr('gameId'), $(this).attr('gameName'),  'Reject')

                var parent = $(this).parents('.switch');
                $('.cb-enable',parent).removeClass('selected');
                $(this).addClass('selected');
                $('.checkbox',parent).attr('checked', false);
            });
        });
        function sendNewGameResponse(gameId,gameName, choice) {
            var newGameResponse = {
                "id" : gameId,
                "gameName": gameName,
                "selectedAction" : choice
            }
            client.send("/app/newGame/response", {}, JSON.stringify(newGameResponse));
        }
    </script>
    <style type="text/css">
    * { margin: 0; padding: 0: }
    body { font-family: Arial, Sans-serif; }
    .field { width: 100%; float: left; margin: 0 0 20px; }
    .field input { margin: 0 0 0 20px; }

    /* Used for the Switch effect: */
    .cb-enable, .cb-disable, .cb-enable span, .cb-disable span { background: url(<g:createLinkTo file="images/switch.gif"></g:createLinkTo>) repeat-x; display: block; float: left; }
    .cb-enable span, .cb-disable span { line-height: 30px; display: block; background-repeat: no-repeat; font-weight: bold; }
    .cb-enable span { background-position: left -90px; padding: 0 10px; }
    .cb-disable span { background-position: right -180px;padding: 0 10px; }
    .cb-disable.selected { background-position: 0 -30px; }
    .cb-disable.selected span { background-position: right -210px; color: #fff; }
    .cb-enable.selected { background-position: 0 -60px; }
    .cb-enable.selected span { background-position: left -150px; color: #fff; }
    .switch label { cursor: pointer; }
    </style>
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

    <!-- Notifications section-->
    <section id="notifcations">

    </section>

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

            <div class="col-md-6">
                <g:if test="${gamesStartedByMe}">
                <div class="well">
                    <h4>Games I Started</h4>
                    <table id="gamesStartedByMe" class="table table-striped">
                        <thead>
                        <tr>
                            <th>
                                Name
                            </th>
                            <th>
                                Born On
                            </th>
                            <th>
                                Dirty On
                            </th>
                            <th>
                                Delete?
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${gamesStartedByMe}">
                            <tr>
                                <td><span style="display: none" id="gameStartedByMe_${it.id}">${it.id}</span> <g:link controller="gameDetails" name="${it.gameName}" params="[id:"${it.id}"]">${it.gameName}</g:link></td>
                                <td>${it.creationTime}</td>
                                <td>${it.lastUpdatedTime}</td>
                                <td><a title="Delete Game" href="javascript:void(0);">
                                    <img alt="Delete Game"
                                         src="<g:createLinkTo file="images/24_empty_trash.jpg"/>">
                                    </a>
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
                </g:if>
                <g:if test="${openInvitations}">
                    <div class="well">
                        <h4>Open Invitations</h4>
                        <table id="openInvitations" class="table table-striped">
                            <thead>
                            <tr>
                                <th>
                                    Name
                                </th>
                                <th>
                                    Invited By
                                </th>
                                <th>
                                    Response
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${openInvitations}">
                                <tr id="openInvite_row_${it.gameName}_${it.id}">
                                    <td><g:link controller="gameDetails" name="${it.gameName}" params="[id:"${it.id}"]">${it.gameName}</g:link></td>
                                    <td>${it.originatorPlayerID}</td>
                                    <td>
                                        <p class="field switch">
                                            <input type="radio" style="display:none" id="newGameResponse_${it.id}_1" name="newGameResponse" />
                                            <input type="radio" style="display:none" id="newGameResponse_${it.id}_2" name="newGameResponse" />
                                            <label for="newGameResponse_${it.id}_1" gameName="${it.gameName}" gameId="${it.id}" class="cb-enable"><span>Accept</span></label>
                                            <label for="newGameResponse_${it.id}_2" gameName="${it.gameName}" gameId="${it.id}" class="cb-disable"><span>Reject</span></label>
                                        </p>
                                    </td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                </g:if>

                <div class="well" id="participatedGames" <g:if test="${!participatedGames}">style="display: none"</g:if>>
                    <h4>Games Participated</h4>
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>
                                Name
                            </th>
                            <th>
                                Invited By
                            </th>
                            <th>
                                Result
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:if test="${participatedGames}">
                            <g:each in="${participatedGames}">
                                <tr>
                                    <td><g:link controller="gameDetails" name="${it.gameName}" params="[id:"${it.id}"]">${it.gameName}</g:link></td>
                                    <td>${it.originatorPlayerID}</td>
                                    <td>
                                        WON
                                    </td>
                                </tr>
                            </g:each>
                        </g:if>
                        </tbody>
                    </table>
                </div>
        </div>

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
</body>
</html>
