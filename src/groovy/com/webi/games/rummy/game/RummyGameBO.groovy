package com.webi.games.rummy.game

import com.webi.games.rummy.entity.GameAcceptStatus
import com.webi.games.rummy.entity.RummyGameEntity

/**
 * Represents the Game Business Object with all relevant fields of the game associated to the player of the Game
 * @author Suman Jakkula
 *
 */
public class RummyGameBO implements IRummyGame {

    long gameId;

    //Optional Game Name
    String gameName;

    //Player to which this game is associated
    String associatedPlayerId;

    Player originatorPlayer;

    // Is game played till the end and completed
    boolean isCompleted;

    // Is game terminated before its completion
    boolean isTerminated;

    Date creationTime;

    Date lastUpdatedTime;

    // Is game terminated before its completion
    boolean isActive;

    boolean handPositionDetermined;

    //Game Accept Status
    GameAcceptStatus gameAcceptanceStatus;

    public RummyGameBO populateFromEntity(RummyGameEntity rummyGameEntity) {
        this.creationTime = rummyGameEntity.getCreationTime();
        this.lastUpdatedTime = rummyGameEntity.getLastUpdatedTime();
        this.gameId = rummyGameEntity.getGameId();
        this.gameName = rummyGameEntity.getGameName();
        this.isActive = rummyGameEntity.isActive();
        this.isCompleted = rummyGameEntity.isCompleted();
        this.isTerminated = rummyGameEntity.isTerminated();
        if (originatorPlayer == null) {
            originatorPlayer = new Player();
        }
        this.originatorPlayer.setEmailId(rummyGameEntity.getOriginatorPlayerID());

        return this;
    }

    @Override
    public Player getWinnerPlayer() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setWinnerPlayer(Player player) {
        // TODO Auto-generated method stub
    }

    @Override
    boolean isCompleted() {
        isCompleted
    }

    @Override
    boolean isTerminated() {
        isTerminated
    }

    @Override
    boolean isActive() {
        isActive
    }
}
