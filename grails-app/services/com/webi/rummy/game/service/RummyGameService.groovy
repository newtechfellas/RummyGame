package com.webi.rummy.game.service

import com.webi.games.rummy.entity.RummyGame
import com.webi.games.rummy.entity.RummyGameAssociatedPlayer
import grails.transaction.Transactional

@Transactional
class RummyGameService {

    def createNewGame(String originatorPlayer, List<String> inviteesPlayerIDs, String gameName) {
        //How about validation? :-)
        RummyGame rummyGame = new RummyGame(gameName: gameName, originatorPlayerID: originatorPlayer, associatedPlayers: [])
        inviteesPlayerIDs?.each { playerId ->
            rummyGame.addToAssociatedPlayers(new RummyGameAssociatedPlayer(playerId: playerId))
        }
        rummyGame.save()
    }

    List<String> getAllInvitedPlayersFor(String playerID) {
        def results = RummyGameAssociatedPlayer.withCriteria {
            projections { property('playerId') }
            'in'('game.id', RummyGame.withCriteria {
                projections { property('id') }
                eq('originatorPlayerID', playerID)
            }
            )
        }
        println "results size is ${results}"
    }
}
