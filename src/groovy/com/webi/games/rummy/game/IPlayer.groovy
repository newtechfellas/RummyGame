package com.webi.games.rummy.game

public interface IPlayer extends Serializable {
	String getName();
	void setName(String name);
	
	String getEmailId();
	void setEmailId(String emailId);

	int getRoundScore();
	void setRoundScore(int score);
}
