package com.webi.games.rummy.game

import com.webi.games.rummy.entity.GameAcceptStatus
import com.webi.games.rummy.entity.RummyGameAssociatedPlayersEntity

public class RummyGameAssociatedPlayerBO {

	//Unique Game Identifier
	long gameId;
	
	//Associated Player
	String playerId;
	
	// Magically matches the player name.
	String playerName;
	
	//Was the game accepted?
	GameAcceptStatus gameAcceptStatus;
	
	//What is the deal order of the game for this player
	int handPositionSequenceNumber;
	
	public RummyGameAssociatedPlayerBO() {
		super();
	}
	
	public RummyGameAssociatedPlayerBO populateFromEntityObject ( RummyGameAssociatedPlayersEntity entity) {
		this.gameId = entity.gameId
		this.gameAcceptStatus = entity.gameAcceptStatus
		this.playerId = entity.playerId
		this.handPositionSequenceNumber = entity.handPositionSequenceNumber
		return this;
	}
}
