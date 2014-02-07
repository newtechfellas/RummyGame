package com.webi.games.rummy.entity

class RummyGameHandPosition {
    long gameId
    String handPositionUserIdListStr

    static constraints = {
        id  name:'gameId'
    }
}
