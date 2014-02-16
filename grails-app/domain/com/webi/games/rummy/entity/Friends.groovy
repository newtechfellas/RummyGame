package com.webi.games.rummy.entity

class Friends {
    String playerId
    String friendPlayerId

    static constraints = {
        friendPlayerId unique: 'playerId'
    }
    static mapping = {
        playerId index: 'player_to_friend_index'
        friendPlayerId index: 'player_to_friend_index'
    }
}
