package com.webi.games.rummy.entity

class RummyGameHandPositionEntity {
    long gameId
    String handPositionUserIdListStr

    static constraints = {
        id  name:'gameId'
    }
}
