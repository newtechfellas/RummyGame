package com.webi.rummy.game.service

import com.webi.games.rummy.entity.Friends
import com.webi.games.rummy.entity.RummyGame
import com.webi.games.rummy.entity.RummyGameAssociatedPlayer
import grails.transaction.Transactional
import org.hibernate.Criteria
import org.hibernate.Session

@Transactional
class RummyGameService {
    def sessionFactory
    def createNewGame(String originatorPlayer, List<String> inviteesPlayerIDs, String gameName) {
        //How about validation? :-)
        RummyGame rummyGame = new RummyGame(gameName: gameName, originatorPlayerID: originatorPlayer, associatedPlayers: [])
        inviteesPlayerIDs?.each { playerId ->
            rummyGame.addToAssociatedPlayers(new RummyGameAssociatedPlayer(playerId: playerId))
        }
        rummyGame.save(flush: true)

        inviteesPlayerIDs?.each {
           new Friends(playerId:originatorPlayer, friendPlayerId: it).save(failOnError: false)
        }
        Session session = sessionFactory.currentSession
        session.flush()
        session.clear()
    }

    List<String> getAllInvitedPlayerIDsFor(String playerID) {
        def results = RummyGameAssociatedPlayer.withCriteria  {
            projections { property('playerId') }
            'in'('game.id', RummyGame.withCriteria {
                projections { property('id') }
                eq('originatorPlayerID', playerID)
            }
            )
            resultTransformer Criteria.DISTINCT_ROOT_ENTITY
        }
        return results
    }

    //
    //returns the friend playerIDs of input playerId
    //friend player = All such players who received invitation from input player
    //                             +
    //                All such player who sent invitation to input player
    //
    List<String> getAllPlayerIDsKnownTo(String playerId) {
        Friends.findAllByPlayerIdOrFriendPlayerId(playerId, playerId)?.collect {
            Friends friends->
                friends.friendPlayerId == playerId ? friends.playerId : friends.friendPlayerId
        }?.sort().unique()
    }
}
