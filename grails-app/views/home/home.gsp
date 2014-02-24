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
<link type="text/css" rel="stylesheet" href="${resource(dir:'css', file:'home.css', absolute: true)}" />
<!-- Combo-handled YUI JS files: -->
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">

<r:require modules="spring-websocket"/>
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
                    var json = JSON.parse(JSON.parse(message.body)); //Not sure why double parsing is required. This is done to eliminate the double quotes in message body
                    for (var i=0 ; i < json.messages.length;i++)
                    {
                        var message = json.messages[i]
                        switch (message.type) {
                            case 'ngrt' :
                               if ( $('#'+'gameStartedByMe_'+message.gameId).exists() ) { //if the game is startedBy current logged in player display the message to him
                                $('#notifcations').append('<p class="ngrt">'+message.fromUser+ ' accepted '+message.gameName+' game</p>')
                               }
                               var openInviteRowElement = $('#'+'openInvitations'+'_'+message.gameId);
                               var participatedGames = $('#participatedGames')
                               if (openInviteRowElement.exists() && participatedGames.exists()) {
                                    openInviteRowElement.find('ul section').replaceWith('<h4 style="float: right">NOT AVAILABLE</h4>')
                                   participatedGames.append(openInviteRowElement)
                                   $(openInviteRowElement).find('td:last').replaceWith('<td>NOT AVAILABLE</td>');
                                   $('#participatedGamesHolder').show()
                               }
                        }
                    }
                });
            });
</r:script>
<script type="text/javascript">
    $(document).ready(function () {
        $(".cb-enable").click(function () {
            sendNewGameResponse($(this).attr('gameId'), $(this).attr('gameName'), 'Accept')

            var parent = $(this).parents('.switch');
            $('.cb-disable', parent).removeClass('selected');
            $(this).addClass('selected');
            $('.checkbox', parent).attr('checked', true);
        });
        $(".cb-disable").click(function () {
            sendNewGameResponse($(this).attr('gameId'), $(this).attr('gameName'), 'Reject')

            var parent = $(this).parents('.switch');
            $('.cb-enable', parent).removeClass('selected');
            $(this).addClass('selected');
            $('.checkbox', parent).attr('checked', false);
        });
    });
    function sendNewGameResponse(gameId, gameName, choice) {
        var newGameResponse = {
            "id": gameId,
            "gameName": gameName,
            "selectedAction": choice
        }
        client.send("/app/newGame/response", {}, JSON.stringify(newGameResponse));
    }
</script>

</head>

<body>

<div class="section">
    <h1>Welcome, ${session.userName} <g:link class="btn btn-warning" uri="/logout">Log out</g:link></h1>
</div>

<div class="pageAllWrap">
    <g:hasErrors bean="${command}">
        <div class="errors">
            <g:renderErrors as="list" bean="${command}"/>
        </div>
    </g:hasErrors>
    <!-- Notifications section-->
    <section id="notifcations" />

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
                    <div class="headerInnerWrap">
                        <g:each in="${gamesStartedByMe}">
                        <nav class="mainNav cf">
                        <span style="display: none" id="gameStartedByMe_${it.id}">${it.id}</span>
                            <ul class="ml1">
                                <g:render template="/home/gameDetailsTemplate" model="[rummyGame:it]"></g:render>
                            %{-- If current player started the game, provide the option to the player to begin game if its not already begun--}%
                                <g:if test="${!it.isActive && it.originatorPlayerID == session['userName']}">
                                    <g:form>
                                        <g:textField name="gameId" hidden="hidden" value="${it.id}" />
                                        <g:actionSubmit value="BeginGame" action="beginGame"></g:actionSubmit>
                                    </g:form>
                                </g:if>
                            </ul>

                        </nav>
                        </g:each>
                        <a style="float: right" title="Delete Game" href="javascript:alert('Idea is to implement drag and drop into this trash bin');">
                            <img alt="Delete Game"
                                 src="<g:createLinkTo file="images/24_empty_trash.jpg"/>">
                        </a>
                    </div>
                </div>
            </g:if>
            <g:if test="${openInvitations}">
                <div class="well">
                    <h4>Open Invitations</h4>
                    <div class="headerInnerWrap" id="openInvitations">
                    <g:each in="${openInvitations}">
                        <nav  class="mainNav cf" id="openInvitations_${it.id}">
                            <ul class="ml1">
                                <g:render template="/home/gameDetailsTemplate" model="[rummyGame:it]"></g:render>
                                <section style="float: right">
                                    <p class="field switch" >
                                        <input type="radio" style="display:none" id="newGameResponse_${it.id}_1"
                                               name="newGameResponse"/>
                                        <input type="radio" style="display:none" id="newGameResponse_${it.id}_2"
                                               name="newGameResponse"/>
                                        <label for="newGameResponse_${it.id}_1" gameName="${it.gameName}" gameId="${it.id}"
                                               class="cb-enable"><span>Accept</span></label>
                                        <label for="newGameResponse_${it.id}_2" gameName="${it.gameName}" gameId="${it.id}"
                                               class="cb-disable"><span>Reject</span></label>
                                    </p>
                                </section>
                            </ul>
                        </nav>
                    </g:each>
                    </div>
                </div>
            </g:if>

            <div class="well" id="participatedGamesHolder" <g:if test="${!participatedGames}">style="display: none"</g:if>>
                <h4>Games Participated</h4>
                    <header style="padding-bottom: 3em">
                        <h4 style="float:left">Game Name</h4>
                        <h4 style="float:right">Game Result</h4>
                    </header>
                    <div class="headerInnerWrap" id="participatedGames">
                    <g:if test="${participatedGames}">
                    <g:each in="${participatedGames}">
                        <nav  class="mainNav cf">
                            <ul class="ml1">
                                <g:render template="/home/gameDetailsTemplate" model="[rummyGame:it]"></g:render>
                                <h4 style="float: right">NOT AVAILABLE</h4>
                            </ul>
                        </nav>
                        </g:each>
                        </g:if>
                    </div>

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
    <g:if test="${activeGames}">
    <g:render template="activeGames" model="[activeGames:activeGames]"></g:render>
    </g:if>
</div>
<div style="display: block;height: 20px" >

</div>
<script>
    function beginGame(gameId) {
        dbg('gameId ='+gameId+ " has begun");
        $.ajax({
            type: 'POST',
            url:'<g:createLink controller="home" action="beginGame"></g:createLink>',
            data: { gameId : gameId }
        }).done(function(data) {
            dbg(gameId+' successfully begun. data='+data);
        });
    }
    $(document).ready(function () {
        $('.ml1a').each(function (i) {
            $(this).attr('id', 'ml1aButton-' + (i + 1));
        });

        $('.navMainOverlay').each(function (i) {
            $(this).attr('id', 'mainNavOverlay-' + (i + 1));
        });
        // MouseOver Function
        $('.ml1a').mouseover(function () {
            var num = this.id.replace('ml1aButton-', '');
            $(this).parent('li').addClass('ml1liHover');
            $('.navMainOverlay').hide();
            $('#mainNavOverlay-' + num).fadeIn(300);
        });
        // MouseLeave Function
        $('.ml1a').bind('mouseleave mousemove', function (e) {
            e.preventDefault();
            var pos = $(this).offset();
            var height = $(this).height();
            var width = $(this).width();

            if (e.pageY < pos.top || e.pageX < pos.left || e.pageX > pos.left + width) {
                $('.navMainOverlay').hide();
                $('.ml1li').removeClass('ml1liHover');
            }
        });
        $('.navMainOverlay').mouseleave(function () {
            $('.ml1li').removeClass('ml1liHover');
            $(this).hide();
        });
        // Focus Function
        $('.ml1a').focus(function () {
            var num = this.id.replace('ml1aButton-', '');
            $('.ml1li').prev().removeClass('ml1liHover');
            $('.ml1li').next().removeClass('ml1liHover');
            $(this).parent('li').addClass('ml1liHover');
            $('.navMainOverlay').hide();
            $('#mainNavOverlay-' + num).fadeIn(300);
        });
        $('.ml1li:last-child a').slice(-1).focusout(function () {
            $('.ml1 li').removeClass('ml1liHover');
            $('.navMainOverlay').hide();
        });
        $('.ml1li:first-child a').focus(function () {
            $('.navMainOverlay').hide();
            $('.ml1li').next().removeClass('ml1liHover');
        });

        $( "#games" ).tabs({
            collapsible: true
        });
    });
</script>
</body>
</html>
