package com.webi.ent.common.vo

/**
 * Created by yuyutsu on 2/4/14.
 */
class GameDetailsUIVO {
    long gameId
    String gameName
    // Comma separated list of associated player names that accepted the game
    String acceptedPlayerNames;
    boolean canGameStart;
    boolean isGameStarted;
    // Comma separated list of associated player names
    String associatedPlayerNames;
    // indicator to render deal position link
    boolean requiresDealPosition;
}
