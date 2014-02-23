package com.webi.rummy.game.service

import com.webi.games.rummy.entity.Friends
import com.webi.games.rummy.entity.GameAcceptStatus
import com.webi.games.rummy.entity.RummyGame
import com.webi.games.rummy.entity.RummyGameAssociatedPlayer
import com.webi.games.rummy.game.NewGameResponseVO
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
            new Friends(playerId: originatorPlayer, friendPlayerId: it).save(failOnError: false)
        }
        Session session = sessionFactory.currentSession
        session.flush()
        session.clear()
    }

    @Transactional(readOnly = true)
    List<String> getAllInvitedPlayerIDsFor(String playerID) {
        def results = RummyGameAssociatedPlayer.withCriteria {
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

    RummyGame beginGame(long gameId, String requestingPlayerId) {
        //check if the input game is started by current logged on player.
        RummyGame game = RummyGame.findByIdAndOriginatorPlayerIDAndIsActive(gameId, requestingPlayerId, false)
        if (game) {
            game.isActive = true
            game.save()
        }
        return game
    }

    //
    //returns the friend playerIDs of input playerId
    //friend player = All such players who received invitation from input player
    //                             +
    //                All such player who sent invitation to input player
    //
    @Transactional(readOnly = true)
    List<String> getAllPlayerIDsKnownTo(String playerId) {
        Friends.findAllByPlayerIdOrFriendPlayerId(playerId, playerId)?.collect {
            Friends friends ->
                friends.friendPlayerId == playerId ? friends.playerId : friends.friendPlayerId
        }?.sort().unique()
    }

    @Transactional(readOnly = true)
    RummyGame getGameDetails(long id, String userName) {
        RummyGame game
        def results = RummyGame.withCriteria { //game started by the player?
            idEquals(id)
            eq('originatorPlayerID', userName)
        }
        if (results) {
            game = results.get(0) //there should be only one game matching the id
        } else {
            results = RummyGameAssociatedPlayer.withCriteria { //game invitation received by the player?
                eq('playerId', userName)
                eq('game.id', id)
            }
            if (results) {
                game = results.get(0).game //there should be only one game matching the id
            }
        }
        return game
    }

    boolean handleNewGameResponse(String userId, NewGameResponseVO newGameResponseVO) {
        def list = RummyGameAssociatedPlayer.withCriteria {
            eq('playerId', userId)
            eq('gameAcceptStatus', GameAcceptStatus.NO_RESPONSE)
            eq('game.id', Long.valueOf(newGameResponseVO.id))
        }
        //There should be only one such game
        if (list) {
            newGameResponseVO.selectedAction == 'Accept' ?
                    (list.get(0).gameAcceptStatus = GameAcceptStatus.ACCEPTED ) : ( list.get(0).gameAcceptStatus = GameAcceptStatus.REJECTED )
            list.get(0).save()
        } else {
            return false
        }
        return true
    }
}
