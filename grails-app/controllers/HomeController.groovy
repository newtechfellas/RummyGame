import app.SecUser
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
        //Perhaps a bad choice to place user object in session for gsp access. Its ok for this app.
        session['user'] = springSecurityService.currentUser
        if (! session['logonEventSent'] ) {
            brokerMessagingTemplate.convertAndSend "/topic/hello", "${session['user'].username}" as String
            session['logonEventSent']=true
        }
        render view: 'index', model: getHomePageModel()
    }

    def startNewGame = {
        StartNewGameCommand command ->
            SecUser loggedInUser = session['user']
            if (command.validate()) {
                List playerIds = command.playerIds.tokenize('\r\n');
                rummyGameService.createNewGame(loggedInUser.username, playerIds, command.gameName)
//                emailService.sendGameInviteEmail(loggedInUser.username, playerIds,command.gameName)
            }
            //refresh the home page
            Map model = getHomePageModel()
            model.put('command', command)
            render view:'index', model : model
    }

    Map getHomePageModel() {
        SecUser loggedInUser = session['user']
        // fetch all the invited players across all games for currently logged in player. Required to display
        // active status of all friends to initiate chat with. Like facebook/google chat
        List<String> invitedPlayers = rummyGameService.getAllInvitedPlayersFor(loggedInUser.username)
        Map model = [invitedPlayers: invitedPlayers, loggedInUser: loggedInUser ]
        return model
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
