package com.webi.games.rummy.entity

enum GameAcceptStatus
{
    NO_RESPONSE('NO_RESPONSE'),
    ACCEPTED('ACCEPTED'),
    REJECTED ('REJECTED')
    String status
    GameAcceptStatus(String status) { this.status = status}
}
class RummyGameAssociatedPlayers {
    String playerId
    GameAcceptStatus gameAcceptStatus = GameAcceptStatus.NO_RESPONSE //default
    //represents the hand position sequence number of
    //the associated player of this game
    int handPositionSequenceNumber

    static belongsTo = [gameId: RummyGame]
    static constraints = {
        id composite: ['gameId', 'playerId']
    }

}
