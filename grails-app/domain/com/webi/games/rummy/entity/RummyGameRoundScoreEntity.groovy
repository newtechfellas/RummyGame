package com.webi.games.rummy.entity

class RummyGameRoundScoreEntity {
    long gameId
    String playerId
    int roundId
    int score
    static constraints = {
        id composite: ['gameId', 'playerId', 'roundId']
    }
}
