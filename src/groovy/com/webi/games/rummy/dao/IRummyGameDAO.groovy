package com.webi.games.rummy.dao
import com.webi.games.rummy.entity.RummyGameAssociatedPlayersEntity
import com.webi.games.rummy.entity.RummyGameEntity
import com.webi.games.rummy.entity.RummyGameHandPositionEntity
import com.webi.games.rummy.game.EmptyNewGameExistsException
import com.webi.games.rummy.game.IPlayer
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
	RummyGameEntity createNewGame(String gameName, IPlayer player, Set<IPlayer> friendsSet) throws EmptyNewGameExistsException;
	
	/**
	 * @param player
	 * @param friendsSet
	 * @return
	 */
	RummyGameEntity getNotStartedOpenGame( IPlayer player);
	
	/**
	 * @param playerIds
	 * @return
	 */
	List<Player> getPlayerNamesForPlayerIDs(List<String> playerIds) ;
	/**
	 * @param gameId
	 * @return
	 */
	List<RummyGameAssociatedPlayersEntity> getAssociatedPlayersListForGame(long gameId) ;
	
	/**
	 * @param gameId
	 * @return
	 */
	RummyGameHandPositionEntity getHandPositionEntity( long gameId);
	
	/**
	 * @param player
	 * @return
	 */
	List<RummyGameEntity> getGamesHistoryForPlayer( IPlayer player);
	
	/**
	 * Fetch all open games of the player
	 * Fetches active games that are not yet completed/terminated
	 * @param player
	 * @return
	 */
	List<RummyGameEntity> getAllOpenGamesStartedByPlayer( IPlayer player);
	
	/**
	 * Deletes a game
	 * 
	 * @param player
	 * @param gameId
	 * @return
	 */
	boolean deleteGame(IPlayer player, RummyGameEntity rummyGame);

	/**
	 * @param player
	 * @return
	 */
	List<IRummyGame> getAllOpenGamesInvitedForPlayer(IPlayer player);
	
	/**
	 * @param gameId
	 * @return
	 */
	RummyGameEntity getGameById(long gameId);

	/**
	 * @param mailManagerUserId
	 * @return
	 */
	String getMailManagerPassword(String mailManagerUserId);
	
}
