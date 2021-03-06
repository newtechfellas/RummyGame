<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Bootstrap core CSS -->
    <g:external uri="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css"/>
    <g:external uri="//codeorigin.jquery.com/jquery-2.0.3.min.js"/>

    <style>
    body {
        padding-top: 50px;
    }
    .section {
        padding: 40px 15px;
        text-align: center;
    }
    </style>
    <title><g:layoutTitle default="App"/></title>
    <g:layoutHead/>
    <r:layoutResources/>
</head>
<script>
    jQuery.fn.exists = function(){return this.length>0;}
    function dbg(message) {
        if ( console ) {
            console.log(message)
        } else {
            alert(message)
        }
    }
</script>

<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><g:link controller="home" action="index">Home</g:link> </li>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</div>

<div class="container">
    <g:layoutBody/>
    <r:layoutResources/>
</div><!-- /.container -->
<footer>

</footer>
</body>
</html>
