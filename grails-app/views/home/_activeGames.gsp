<style>
.card {
    float: left;
    padding-left: -5px;
    margin-left: -85px;
    width: 100px;
    height: 150px;
    background-image: url("../images/cards/CardsBack.jpg");
    border: 1px outset #255b17;
    background-size: 100% 100%;
}
</style>


<div class="ui-tabs ui-widget ui-widget-content ui-corner-all ui-tabs-collapsible" id="games">
    <ul role="tablist" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
        <g:each in="${activeGames}" var="activeGame" status="index">
            <li aria-selected="false" aria-labelledby="ui-id-${index}" aria-controls="tabs-${index}" tabindex="-1" role="tab" class="ui-state-default ui-corner-top">
                <a id="ui-id-${index}" tabindex="-1" role="presentation" class="ui-tabs-anchor" href="#game-${activeGame.id}">${activeGame.gameName}</a></li>
        </g:each>
    </ul>

    <g:each in="${activeGames}" var="activeGame" status="index">
        <div aria-hidden="true" aria-expanded="false" role="tabpanel" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-labelledby="ui-id-${index}" id="game-${activeGame.id}">
        %{--If all accepted players are not active at this time, show a message --}%
            <g:if test="${!activeGame.isHandPositionDetermined}">
                <p><strong>Choose Deal Sequence</strong> <input id="shuffleCards" type="button" value="Shuffle" /></p>
                <div id="startup_deck" style="height:100%; overflow: auto">
                </div>
            </g:if>
        </div>
    </g:each>

</div>

<script>
    function drawStartupDeck() {
        var drawCard = function (startupDeckDiv,cardIndex,marginTop) {
            if ( cardIndex == 0 ) {
                startupDeckDiv.append('<div class="card" style="margin-left: 0px"></div>')
            }
            else {
                startupDeckDiv.append('<div class="card" style="margin-top: '+cardIndex*marginTop+'px;" ></div>')
            }
        };
        var maxCardCount = 52;
        var startupDeckDiv = $('#startup_deck')
        //Draw the initial deck
        for ( var i=0; i < maxCardCount ; i+= 1 )
        {
            drawCard(startupDeckDiv,i,0);  //  marginTop = 3px separated
        }

        var shuffleCards = function() {
            //clear the complete canvas
            startupDeckDiv.empty();
            var cardIndex=0;
            var shuffleCardsTimer = setInterval(function() {
                drawCard(startupDeckDiv,cardIndex,0);
                if ( cardIndex == maxCardCount ) {
                    console.log(cardIndex);
                    clearInterval(shuffleCardsTimer);
                }
                cardIndex++;
            }, 20);
        };
        //Event handler for shuffling
        $('#shuffleCards').click( function () {
            shuffleCards();
        });
    }
    $(document).ready(drawStartupDeck);
</script>
