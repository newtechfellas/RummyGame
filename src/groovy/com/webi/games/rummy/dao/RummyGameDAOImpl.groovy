package com.webi.games.rummy.dao

import com.webi.games.rummy.entity.RummyGame
import com.webi.games.rummy.entity.RummyGameAssociatedPlayer
import com.webi.games.rummy.entity.RummyGameHandPosition
import com.webi.games.rummy.game.EmptyNewGameExistsException
import com.webi.games.rummy.game.IRummyGame
import com.webi.games.rummy.game.Player

/**
 * DAO implementation for working with Rummy Game Database. APIs on this class expect presence of transaction.
 * Transactional behavior is controlled at service level.
 *
 * @author Webi
 *
 */
public class RummyGameDAOImpl implements IRummyGameDAO {

    @Override
    RummyGame createNewGame(String gameName, Player player, Set<Player> friendsSet) throws EmptyNewGameExistsException {
        return null
    }

    @Override
    RummyGame getNotStartedOpenGame(Player player) {
        return null
    }

    @Override
    List<Player> getPlayerNamesForPlayerIDs(List<String> playerIds) {
        return null
    }

    @Override
    List<RummyGameAssociatedPlayer> getAssociatedPlayersListForGame(long gameId) {
        return null
    }

    @Override
    RummyGameHandPosition getHandPositionEntity(long gameId) {
        return null
    }

    @Override
    List<RummyGame> getGamesHistoryForPlayer(Player player) {
        return null
    }

    @Override
    List<RummyGame> getAllOpenGamesStartedByPlayer(Player player) {
        return null
    }

    @Override
    boolean deleteGame(Player player, RummyGame rummyGame) {
        return false
    }

    @Override
    List<IRummyGame> getAllOpenGamesInvitedForPlayer(Player player) {
        return null
    }

    @Override
    RummyGame getGameById(long gameId) {
        return null
    }

    @Override
    String getMailManagerPassword(String mailManagerUserId) {
        return null
    }
}
