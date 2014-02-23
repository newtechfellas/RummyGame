<li class="ml1li">
    <a href="#" class="ml1a">${rummyGame.gameName}<span class="arrow"></span></a>
    <div class="navMainOverlay cf">
        <ul class="ml2">
            <li>
                <ul class="ml3">
                    <li>Started By:${rummyGame.originatorPlayerID} on ${rummyGame.creationTime}</li>
                    <g:if test="${rummyGame.lastUpdatedTime}">
                        <li>Last Updated:${rummyGame.lastUpdatedTime}</li>
                    </g:if>
                    <g:if test="${rummyGame.isActive}">
                        <li>Game is active at present</li>
                    </g:if>
                    <g:if test="${rummyGame.isCompleted}">
                        <li>Game is completed</li>
                    </g:if>
                </ul>
            </li>
            <li>
                <a href="javascript:void(0);">Players</a>
                <ul class="ml3">
                    <g:each in="${rummyGame.associatedPlayers}" var="associatedPlayer">
                        <li>${associatedPlayer.playerId}
                        <g:if test="${associatedPlayer.gameAcceptStatus == com.webi.games.rummy.entity.GameAcceptStatus.ACCEPTED}">
                            &nbsp;Accepted
                        </g:if>
                        </li>
                    </g:each>
                </ul>
            </li>

        </ul>
    </div>
</li>
