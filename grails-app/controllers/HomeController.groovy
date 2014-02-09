import app.SecUser
import com.webi.rummy.game.service.EmailService
import com.webi.rummy.game.service.RummyGameService
import grails.plugins.springsecurity.SpringSecurityService
import grails.validation.Validateable

class HomeController {

    SpringSecurityService springSecurityService
    EmailService emailService
    RummyGameService rummyGameService

    def index = {
        SecUser loggedInUser = springSecurityService.currentUser
        //Perhaps a bad choice to place user object in session for gsp access. Its ok for this app.
        session['user'] = loggedInUser
        // fetch all the invited players across all games for currently logged in player. Required to display
        // active status of all friends to initiate chat with. Like facebook/google chat
        List<String> invitedPlayers = rummyGameService.getAllInvitedPlayersFor(loggedInUser.username)

    }

    def startNewGame = {
        StartNewGameCommand command ->
            if (command.validate()) {
                List playerIds = command.playerIds.tokenize('\r\n');
                rummyGameService.createNewGame(session.user.username, playerIds, command.gameName)
//                emailService.sendGameInviteEmail(session.user.username, playerIds,command.gameName)
            }
            List<String> invitedPlayers = rummyGameService.getAllInvitedPlayersFor(session['user'].username)
            render view: "index", model: [command: command]
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
