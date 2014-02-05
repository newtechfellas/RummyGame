package com.webi.games.rummy.entity

class PlayerPersonalInfoEntity {
    String playerId
    String playerName
    static constraints = {
        id name:'playerId'
    }
}
