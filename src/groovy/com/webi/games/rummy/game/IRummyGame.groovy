package com.webi.games.rummy.game;

/**
 * Game Object interface
 *
 * @author Suman Jakkula
 *
 */
public interface IRummyGame {

    Player getWinnerPlayer();

    long getGameId();

    String getGameName();

    Player getOriginatorPlayer();

    boolean isCompleted();

    boolean isHandPositionDetermined();

    boolean isTerminated();

    Date getCreationTime();

    Date getLastUpdatedTime();

    public boolean isActive();
}
