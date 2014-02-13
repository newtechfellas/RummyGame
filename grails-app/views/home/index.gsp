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
    .chat_player { }
    </style>

    <r:require modules="jquery, spring-websocket" />
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
            <g:renderErrors as="list" bean="${command}" />
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
                                <g:textArea name="playerIds" title="Enter Player Mail Ids One Per Row" placeholder="Enter Player EmailIds One Per Row" cols="70" rows="5"></g:textArea>
                                <br/>
                                <br>
                                Game Name <g:textField name="gameName"></g:textField>
                            </fieldset>
                        </div>
                        <g:submitButton name="Create New Game And Send Invite" />
                    </section>
                </g:form>
            </div>
        </div>
        <!-- Existing Games open  -->
        <div class="col-md-6">
            <div class="well">
                <h4>Games History</h4>
                <table id="gplus-profile" class="table table-striped">
                    <thead>
                    <tr>
                        <th>
                            Description
                        </th>
                        <th>
                            Value
                        </th>
                    </tr>
                    </thead>
                    <tbody></tbody>
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
