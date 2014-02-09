package com.webi.games.rummy.dao

import com.webi.games.rummy.entity.RummyGame
import com.webi.games.rummy.entity.RummyGameAssociatedPlayer

import com.webi.games.rummy.entity.RummyGameHandPosition
import com.webi.games.rummy.game.EmptyNewGameExistsException

import com.webi.games.rummy.game.IRummyGame
import com.webi.games.rummy.game.Player

public interface IRummyGameDAO {

	/**
	 * Creates A New Game
	 * 
	 * @param player - Player initiating the game
	 * @param friendsSet - Players participating in the game
	 * 
	 * @return 	true if the game creation is successful
	 * 			false otherwise
	 */
	RummyGame createNewGame(String gameName, Player player, Set<Player> friendsSet) throws EmptyNewGameExistsException;
	
	/**
	 * @param player
	 * @param friendsSet
	 * @return
	 */
	RummyGame getNotStartedOpenGame( Player player);
	
	/**
	 * @param playerIds
	 * @return
	 */
	List<Player> getPlayerNamesForPlayerIDs(List<String> playerIds) ;
	/**
	 * @param gameId
	 * @return
	 */
	List<RummyGameAssociatedPlayer> getAssociatedPlayersListForGame(long gameId) ;
	
	/**
	 * @param gameId
	 * @return
	 */
	RummyGameHandPosition getHandPositionEntity( long gameId);
	
	/**
	 * @param player
	 * @return
	 */
	List<RummyGame> getGamesHistoryForPlayer( Player player);
	
	/**
	 * Fetch all open games of the player
	 * Fetches active games that are not yet completed/terminated
	 * @param player
	 * @return
	 */
	List<RummyGame> getAllOpenGamesStartedByPlayer( Player player);
	
	/**
	 * Deletes a game
	 * 
	 * @param player
	 * @param gameId
	 * @return
	 */
	boolean deleteGame(Player player, RummyGame rummyGame);

	/**
	 * @param player
	 * @return
	 */
	List<IRummyGame> getAllOpenGamesInvitedForPlayer(Player player);
	
	/**
	 * @param gameId
	 * @return
	 */
	RummyGame getGameById(long gameId);

	/**
	 * @param mailManagerUserId
	 * @return
	 */
	String getMailManagerPassword(String mailManagerUserId);
	
}
