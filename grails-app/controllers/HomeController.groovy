import app.SecUser
import com.webi.ent.util.RummyGameUtil
import com.webi.games.rummy.game.Player
import com.webi.rummy.game.service.EmailService
import com.webi.rummy.game.service.RummyGameService
import grails.plugins.springsecurity.SpringSecurityService
import grails.validation.Validateable
import org.springframework.messaging.simp.SimpMessagingTemplate

class HomeController {

    SpringSecurityService springSecurityService
    EmailService emailService
    RummyGameService rummyGameService
    SimpMessagingTemplate brokerMessagingTemplate

    def index = {
        String userName = ((SecUser) springSecurityService.currentUser).username

        if (!session['userName']) {
            RummyGameUtil.addToLoggedInUsersTable(userName)
            brokerMessagingTemplate.convertAndSend "/topic/hello", userName
            session['userName'] = userName
        }
        render view: 'index', model: getHomePageModel()
    }

    def startNewGame = {
        StartNewGameCommand command ->
            String loggedInUser = session['userName']
            if (command.validate()) {
                List playerIds = command.playerIds.tokenize('\r\n');
                rummyGameService.createNewGame(loggedInUser, playerIds, command.gameName)
//                emailService.sendGameInviteEmail(loggedInUser.username, playerIds,command.gameName)
            }
            //refresh the home page
            Map model = getHomePageModel()
            model.put('command', command)
            render view: 'index', model: model
    }

    Map getHomePageModel() {
        String loggedInUser = session['userName']
        // fetch all the invited players across all games for currently logged in player. Required to display
        // active status of all friends to initiate chat with. Like facebook/google chat
        List<Player> currentPlayerFriends = rummyGameService.getAllPlayerIDsKnownTo(loggedInUser)?.collect {
            new Player(emailId: it, isLoggedInNow: RummyGameUtil.isUsedLoggedInNow(it))
        }
        [currentPlayerFriends: currentPlayerFriends, loggedInUser: loggedInUser]
    }
}

@Validateable
class StartNewGameCommand {
    String playerIds
    String gameName
    static constraints = {
        playerIds nullable: false, blank: false
        gameName nullable: false
    }
}
