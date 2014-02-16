package com.webi.games.rummy.game

/**
 * Created by yuyutsu on 2/14/14.
 */
class NewGameResponseVO {
    String id
    String gameName
    String selectedAction


    @Override
    public String toString() {
        return "NewGameResponseVO{" +
                "id=" + id +
                "gameName=" + gameName +
                ", selectedAction='" + selectedAction + '\'' +
                '}';
    }
}
