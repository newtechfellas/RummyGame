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
    </style>
</head>

<body>

<div class="container">

    <div class="section">
        <h1>Welcome, ${session.user} <g:link class="btn btn-warning" uri="/logout">Log out</g:link></h1>
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
    </div>

    <script>
        //hack to prevent toast message from reappearing
        <g:if test="${session.authProvider == 'Google'}">
        window.___gcfg = { isSignedOut: true };

        function autologin(){
            if( !$('#signinButton').size() ){
                gapi.auth.authorize({
                    client_id: '769451899617-eli6a32e4ll9nuqumh3cgca975h3k7n4.apps.googleusercontent.com',
                    scope: 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email',
                    immediate: true,
                    response_type: "code token"
                }, signinCallback );
            }
        }
        (function() {
            var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
            po.src = 'https://apis.google.com/js/client:plusone.js?onload=autologin';
            var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
        })();

        function signinCallback(authResult){

            gapi.client.load('oauth2', 'v2', function() {
                var request = gapi.client.oauth2.userinfo.get();
                request.execute(function(obj){
                    console.log(obj);

                    if( obj.email != "${user.username}" ){
                        location.href = '/logout';
                        return;
                    }

                    if (obj.email) {
                        $('#gplus-profile tbody').append("<tr><td>Email</td><td>" + obj.email + "</td>");
                    }

                    gapi.client.load('plus', 'v1', function(){
                        var request = gapi.client.plus.people.get({
                            'userId': 'me'
                        });
                        request.execute(function(resp) {
                            console.log(resp);
                            var tbody = $('#gplus-profile tbody');
                            if(resp.name){
                                if( resp.name.givenName ){
                                    tbody.append("<tr><td>Given Name</td><td>"+resp.name.givenName+"</td>");
                                }
                                if( resp.name.familyName ){
                                    tbody.append("<tr><td>Family Name</td><td>"+resp.name.familyName+"</td>");
                                }
                            }
                            if(resp.image && resp.image.url){
                                tbody.append("<tr><td>Photo</td><td><img src='"+resp.image.url+"'/></td>");
                            }
                            if(resp.url){
                                tbody.append("<tr><td>Google+ Profile</td><td><a href='"+resp.url+"'>"+resp.url+"</a></td>");
                            }
                            if(resp.result && resp.result.gender){
                                tbody.append("<tr><td>Gender</td><td>"+resp.result.gender+"</td>");
                            }
                        });
                    });
                });
            });

        }
        </g:if>
    </script>

</div><!-- /.container -->

</body>
</html>
