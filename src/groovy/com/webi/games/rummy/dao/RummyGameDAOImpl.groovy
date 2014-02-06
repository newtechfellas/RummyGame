package com.webi.games.rummy.dao

import com.webi.ent.registration.UserRegistryEntity
import com.webi.games.rummy.entity.MailManagerEntity
import com.webi.games.rummy.entity.RummyGameAssociatedPlayersEntity
import com.webi.games.rummy.entity.RummyGameEntity
import com.webi.games.rummy.entity.RummyGameHandPositionEntity
import com.webi.games.rummy.game.*
import org.hibernate.Criteria
import org.hibernate.Query
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import javax.sql.DataSource
import java.sql.Date

/**
 * DAO implementation for working with Rummy Game Database. APIs on this class expect presence of transaction.
 * Transactional behavior is controlled at service level.
 * 
 * @author Webi
 *
 */
@Repository
public class RummyGameDAOImpl implements IRummyGameDAO {
	
	final static String HQL_OPEN_GAMES_FOR_PLAYER = "FROM RummyGameEntity p WHERE p.originatorPlayerID=:originatorPlayerId AND ( isCompleted=false OR isTerminated=false ) " ;
	
	// Use a right join between RummyGameTbl and RummyGameAssociatedPlayers_Tbl
	//TOOD: Bad query.. should not use UserRegistryEntity. Consider using PlayerPersonalInfoEntity  
	final static String HQL_OPEN_GAMES_INVITED_FOR_PLAYER =
            """ FROM RummyGameEntity  game , RummyGameAssociatedPlayersEntity assocPlayers, UserRegistryEntity playerInfo
                WHERE game.gameId=assocPlayers.gameId AND game.originatorPlayerID != :playerId AND assocPlayers.playerId=:playerId
                AND playerInfo.email=game.originatorPlayerID"""
	final static String HQL_GAMES_HISTORY_FOR_PLAYER = "FROM RummyGameEntity p WHERE p.originatorPlayerID=:originatorPlayerId AND ( isCompleted=true OR isTerminated=true ) ";
	final static String HQL_NOT_STARTED_GAME_FOR_PLAYER = "FROM RummyGameEntity p WHERE p.originatorPlayerID=:originatorPlayerId AND isActive=false";
	
	final static String HQL_GET_PLAYER_NAMES_FROM_IDS = "From UserRegistryEntity playerInfo WHERE playerInfo.email in (:playerIds )";
	

	@Autowired
    SessionFactory sessionFactory;
	
	@Autowired
	DataSource datasource;
	
	/* (non-Javadoc)
	 * @see com.webi.games.rummy.dao.IRummyGameDAO#createNewGame(java.lang.String, com.webi.games.rummy.game.IPlayer, java.util.Set)
	 */
	@Override
	public RummyGameEntity createNewGame(String gameName, IPlayer player, final Set<IPlayer> friendsSet)  throws EmptyNewGameExistsException {
		
//		RummyGameEntity openGame= getNotStartedOpenGame(player);
//		if ( openGame != null ) {
//			throw new EmptyNewGameExistsException();
//		}
		
		final RummyGameEntity rummyGameEntity = createNewRummyEntityObject(gameName, player);
		Long gameID = (Long)sessionFactory.getCurrentSession().save(rummyGameEntity);
		//Create Associations to the newly created game
		for (Iterator<IPlayer> iterator = friendsSet.iterator(); iterator.hasNext();) 
		{
			IPlayer associatedPlayer = iterator.next();
			RummyGameAssociatedPlayersEntity rummyGameAssociatedPlayersEntity =
					new RummyGameAssociatedPlayersEntity(gameID.longValue(), associatedPlayer.getEmailId());
			sessionFactory.getCurrentSession().save(rummyGameAssociatedPlayersEntity);
		}
		
		return rummyGameEntity; 
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<RummyGameAssociatedPlayersEntity> getAssociatedPlayersListForGame(long gameId) {
		
		//Simple enough query... lets try Hibernate Criteria approach
		final Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(RummyGameAssociatedPlayersEntity.class);
		return criteria.add(Restrictions.eq("gameId", gameId)).list();
	}
	
	@Override
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public List<Player> getPlayerNamesForPlayerIDs(List<String> playerIds) {
		final List<Player> playersWithNamesPopulated = new ArrayList<Player>();
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery(HQL_GET_PLAYER_NAMES_FROM_IDS).setParameterList("playerIds", playerIds);
		List<UserRegistryEntity> usersList = query.list();
		for ( UserRegistryEntity entity : usersList ) {
			Player player = new Player();
			player.setEmailId(entity.getUserId());
			player.setName(entity.getUserName());
			playersWithNamesPopulated.add(player);
		}

		return playersWithNamesPopulated;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public String getMailManagerPassword(final String mailManagerUserId) {
		final Session session = sessionFactory.getCurrentSession();
		MailManagerEntity mailManagerEntity = (MailManagerEntity)session.get(MailManagerEntity.class, mailManagerUserId);
		String password = null;
		if ( mailManagerEntity != null ) {
			password = mailManagerEntity.getPassword();
		}
		return password;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public RummyGameEntity getGameById(final long gameId) {
		return (RummyGameEntity)sessionFactory.getCurrentSession().get(RummyGameEntity.class, gameId);
	}

	/* (non-Javadoc)
	 * @see com.webi.games.rummy.dao.IRummyGameDAO#getAllOpenGamesStartedByPlayer(com.webi.games.rummy.game.IPlayer)
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<RummyGameEntity> getAllOpenGamesStartedByPlayer( IPlayer player) {
		
		Session session = sessionFactory.getCurrentSession();
		String originatorPlayerId = player.getEmailId();
		final Query query = session.createQuery(HQL_OPEN_GAMES_FOR_PLAYER);
		return query.setString("originatorPlayerId",originatorPlayerId).list();
	}
	
	/* (non-Javadoc)
	 * @see com.webi.games.rummy.dao.IRummyGameDAO#getAllOpenGamesInvitedForPlayer(com.webi.games.rummy.game.IPlayer)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<IRummyGame> getAllOpenGamesInvitedForPlayer(final IPlayer player) {
		final Session session = sessionFactory.getCurrentSession();
		final String playerId = player.getEmailId();
		final Query query = session.createQuery(HQL_OPEN_GAMES_INVITED_FOR_PLAYER);
		query.setString("playerId",playerId);
		//query.setString("gameAcceptStatus", GameAcceptStatus.NO_RESPONSE.toString());
		
		final List<IRummyGame> openGamesList  = new ArrayList<IRummyGame>();
		List<Object[]> associatedPlayersEntityList = query.list();
		for (Iterator<Object[]> iterator = associatedPlayersEntityList.iterator(); iterator.hasNext();) {
			Object [] resultPair = iterator.next();
			RummyGameBO rummyGameBO =new RummyGameBO();
			rummyGameBO.populateFromEntity((RummyGameEntity)resultPair[0]);
			//Originator Player
			Player originatorPlayer = new Player();
			
			UserRegistryEntity userRegistryEntity  =  ((UserRegistryEntity)resultPair[2]);
			originatorPlayer.setEmailId(userRegistryEntity.getUserId());
			originatorPlayer.setName(userRegistryEntity.getUserName());
			rummyGameBO.setOriginatorPlayer(originatorPlayer);
			
			rummyGameBO.setAssociatedPlayerId(playerId);
			
			rummyGameBO.setGameAcceptanceStatus(((RummyGameAssociatedPlayersEntity)resultPair[1]).getGameAcceptStatus());
			openGamesList.add(rummyGameBO);
		}
		return openGamesList;
	}

	public List<RummyGameEntity> getGamesHistoryForPlayer( IPlayer player) {
		Session session = sessionFactory.getCurrentSession();
		String originatorPlayerId = player.getEmailId();
		final Query query = session.createQuery(HQL_GAMES_HISTORY_FOR_PLAYER );
		return query.setString("originatorPlayerId",originatorPlayerId).list();
	}
	
	@Override
	@Transactional (propagation=Propagation.REQUIRED)
	public boolean deleteGame(IPlayer player, RummyGameEntity rummyGame) {
		final RummyGameEntity rummyGameEntity = new RummyGameEntity();
		rummyGameEntity.setGameId(rummyGame.getGameId());
		Session session = sessionFactory.getCurrentSession();
		
		//delete the main game entry
		session.delete(rummyGameEntity);
		return true;
	}

	@Override
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public RummyGameEntity getNotStartedOpenGame(IPlayer player) {
		RummyGameEntity rummyGame = null;
		String originatorPlayerId = player.getEmailId();
		Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery(HQL_NOT_STARTED_GAME_FOR_PLAYER);
		query.setString("originatorPlayerId",originatorPlayerId);

		List<RummyGameEntity> openGames = query.list();
		if ( openGames != null && openGames.isEmpty() == false ) 
		{
			rummyGame = openGames.get(0);
		}
		
		return rummyGame;
	}
	
	@Deprecated
	@Override
	public RummyGameHandPositionEntity getHandPositionEntity(final long gameId) {
		final Session session = sessionFactory.getCurrentSession();
		return (RummyGameHandPositionEntity)session.get(RummyGameHandPositionEntity.class, gameId);
	}
	
	
	/**
	 * Helper method to instantiate a new game entity object
	 * 
	 * @param gameName
	 * @param player
	 * @return
	 */
	RummyGameEntity createNewRummyEntityObject(String gameName, IPlayer player) 
	{
		RummyGameEntity rummyGameEntity = new RummyGameEntity();
		Date currentTime = new Date(System.currentTimeMillis());
		rummyGameEntity.setCreationTime(currentTime);
		rummyGameEntity.setLastUpdatedTime(currentTime);
		rummyGameEntity.setOriginatorPlayerID(player.getEmailId());
		rummyGameEntity.setGameName(gameName);
		rummyGameEntity.setActive(false);
		
		return rummyGameEntity;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	
	
}
