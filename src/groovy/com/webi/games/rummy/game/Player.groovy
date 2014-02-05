package com.webi.games.rummy.game;

public class Player implements IPlayer {
    String emailId;
	String name;
	int roundScore;

	public Player() {}
	public Player(String emailId) {
		this.emailId = emailId;
	}
}
