<html>
<head>
	<meta name='layout' content='main'/>
	<title>Login</title>
    <style>
    body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #eee;
    }

    .form-signin {
        max-width: 330px;
        padding: 15px;
        margin: 0 auto;
    }
    .form-signin .form-signin-heading,
    .form-signin .checkbox {
        margin-bottom: 10px;
    }
    .form-signin .checkbox {
        font-weight: normal;
    }
    .form-signin .form-control {
        position: relative;
        font-size: 16px;
        height: auto;
        padding: 10px;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
    }
    .form-signin .form-control:focus {
        z-index: 2;
    }
    .form-signin input[type="text"] {
        margin-bottom: -1px;
        border-bottom-left-radius: 0;
        border-bottom-right-radius: 0;
    }
    .form-signin input[type="password"] {
        margin-bottom: 10px;
        border-top-left-radius: 0;
        border-top-right-radius: 0;
    }
    #signinButton{
        margin-top: 10px;
    }
    </style>
</head>

<body>

<script>
    //hack to prevent toast message from reappearing
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



    function signinCallback(authResult) {

        if (authResult['access_token'] && authResult['g-oauth-window']) {

            console.log( authResult );

            // Successfully authorized

            gapi.client.load('oauth2', 'v2', function() {
                var request = gapi.client.oauth2.userinfo.get();
                request.execute(function(obj){

                    $.post('/RummyGame/login/plus', {
                        code: authResult.code,
                        email: obj.email,
                        gPlusId: obj.id
                    }, function(data){
                        if( data.status == 'success' ){
                            location.href = data.redirectUri;
                        }
                    })
                });
            });

        } else if (authResult['error']) {
            // There was an error.
            // Possible error codes:
            //   "access_denied" - SecUser denied access to your app
            //   "immediate_failed" - Could not automatically log in the user
            // console.log('There was an error: ' + authResult['error']);
        }
    }

    function populateData(){

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
    }
</script>

<br/>
<sec:ifNotGranted roles="ROLE_USER">
    <facebookAuth:connect />
</sec:ifNotGranted>
<sec:ifAllGranted roles="ROLE_USER">
    Welcome <sec:username/>! (<g:link uri="/j_spring_security_logout">Logout</g:link>)
</sec:ifAllGranted>


<div id="signinButton">
    <span
            class="g-signin"
            data-callback="signinCallback"
            data-clientid="769451899617-eli6a32e4ll9nuqumh3cgca975h3k7n4.apps.googleusercontent.com"
            data-cookiepolicy="single_host_origin"
            data-requestvisibleactions="http://schemas.google.com/AddActivity"
            data-scope="https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email">
    </span>
</div>





</body>
</html>
