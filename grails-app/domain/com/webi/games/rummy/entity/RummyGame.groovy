package com.webi.games.rummy.entity

class RummyGame {
    String gameName //Optional Game Name
    String originatorPlayerID  //Player That created the Game
    boolean isCompleted // Is game played till the end and completed
    boolean isTerminated // Is game terminated before its completion
    Date creationTime = new Date()
    Date lastUpdatedTime
    boolean isActive; // Is game being played currently

    static hasMany = [associatedPlayers : RummyGameAssociatedPlayer]
    static constraints = {
        lastUpdatedTime nullable: true
    }
}
