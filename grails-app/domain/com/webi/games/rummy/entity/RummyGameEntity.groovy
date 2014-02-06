package com.webi.games.rummy.entity

class RummyGameEntity {
    long gameId
    String gameName //Optional Game Name
    String originatorPlayerID  //Player That created the Game
    boolean isCompleted // Is game played till the end and completed
    boolean isTerminated // Is game terminated before its completion
    Date creationTime = new Date()
    Date lastUpdatedTime
    boolean isActive; // Is game being played currently

    static constraints = {
        id name: 'gameId', generator: 'hilo'
        lastUpdatedTime nullable: true
    }
}
