package com.webi.ent.util

import com.webi.games.rummy.game.IPlayer
import com.webi.games.rummy.game.Player
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

/**
 * Created by yuyutsu on 2/4/14.
 */
class RummyGameUtil {

/**
 * Current logged on player
 * @return
 */
    public static IPlayer getCurrentSessoinPlayer() {
        new Player(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }
}
