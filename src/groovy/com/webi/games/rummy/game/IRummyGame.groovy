package com.webi.games.rummy.game;

/**
 * Game Object interface
 * 
 * @author Suman Jakkula
 *
 */
public interface IRummyGame {
	
	IPlayer getWinnerPlayer();
	
	long getGameId();
	
	String getGameName() ;

	IPlayer getOriginatorPlayer();

	boolean isCompleted() ;
	
	boolean isHandPositionDetermined();
	
	boolean isTerminated();

	Date getCreationTime() ;

	Date getLastUpdatedTime() ;

	public boolean isActive() ;
}
