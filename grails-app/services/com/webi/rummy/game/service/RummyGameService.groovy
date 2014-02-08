package com.webi.rummy.game.service

import com.webi.games.rummy.entity.RummyGame
import com.webi.games.rummy.entity.RummyGameAssociatedPlayers
import grails.transaction.Transactional

@Transactional
class RummyGameService {

    def createNewGame(String originatorPlayer, List<String> inviteesPlayerIDs, String gameName ) {
        //How about validation? :-)
        RummyGame rummyGame = new RummyGame(gameName: gameName, originatorPlayerID: originatorPlayer, associatedPlayers:[])
        inviteesPlayerIDs?.each { playerId->
            rummyGame.addToAssociatedPlayers(new RummyGameAssociatedPlayers(playerId:playerId))
        }
        rummyGame.save()
    }
}
