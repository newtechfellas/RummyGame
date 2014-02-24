package com.webi.ent.util

import com.webi.games.rummy.game.Card
import com.webi.games.rummy.game.Player
import com.webi.games.rummy.game.Type
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

    public static List<Card> getNewHandPositionDeck() {
        LinkedHashSet randomIndices = getRandomIndices()
        List<Card> deck = (1..52).collect { int number ->
            new Card( index:randomIndices.getAt(number-1), type: getTypeForNumber(number), value: number )
        }
        return deck
    }

    //first 13 become Clubs, followed by Diamonds, Hearts, Spades
    private static Type getTypeForNumber(int number) {
        return ( number > 39  ? Type.Spades : ( number > 26 ? Type.Hearts : ( number > 13 ? Type.Diamonds : Type.Clubs)))
    }

    public static LinkedHashSet getRandomIndices() {
        //choose random sequence for 1-52 numbers
        Random random = new Random()
        LinkedHashSet randomIndices = []
        52.times {
            while (true) { //TODO: potential infinite loop
                if (randomIndices.add(random.nextInt(52)+1))
                    break
            }
        }
        return randomIndices
    }
}
