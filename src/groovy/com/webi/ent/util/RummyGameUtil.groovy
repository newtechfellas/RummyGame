package com.webi.ent.util
import com.webi.games.rummy.game.Player
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

/**
 * Created by yuyutsu on 2/4/14.
 */
class RummyGameUtil {

    //Yikes..this is for the initial phase implementation. Need to use a DB for scalability
    public static final Hashtable loggedInUsersTable = new Hashtable()

    public static void addToLoggedInUsersTable(String userName) {
        userName && loggedInUsersTable.put(userName, userName)
    }

    public static void removeFromLoggedInUsersTable(String userName) {
        userName && loggedInUsersTable.remove(userName)
    }

    public static boolean isUsedLoggedInNow(String userName) {
        userName && loggedInUsersTable.contains(userName)
    }

    public static Player getCurrentSessoinPlayer() {
        new Player(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }
}
