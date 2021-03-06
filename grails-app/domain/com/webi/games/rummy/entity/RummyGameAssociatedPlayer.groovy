package com.webi.games.rummy.entity

enum GameAcceptStatus
{
    NO_RESPONSE('NO_RESPONSE'),
    ACCEPTED('ACCEPTED'),
    REJECTED ('REJECTED')
    String status
    GameAcceptStatus(String status) { this.status = status}
}

class RummyGameAssociatedPlayer {
    String playerId
    GameAcceptStatus gameAcceptStatus = GameAcceptStatus.NO_RESPONSE //default
    //represents the hand position sequence number of
    //the associated player of this game
    int handPositionSequenceNumber
    static belongsTo = [game: RummyGame]
    static constraints = {
        id compsite: ['game', 'playerId']
    }
}
