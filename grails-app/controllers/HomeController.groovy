import app.SecUser
import com.google.gson.Gson
import com.webi.ent.util.RummyGameUtil
import com.webi.games.rummy.entity.GameAcceptStatus
import com.webi.games.rummy.entity.RummyGame
import com.webi.games.rummy.entity.RummyGameAssociatedPlayer
import com.webi.games.rummy.game.GenericTopicVO
import com.webi.games.rummy.game.NewGameResponseVO
import com.webi.games.rummy.game.NotificationVO
import com.webi.games.rummy.game.Player
import com.webi.rummy.game.service.EmailService
import com.webi.rummy.game.service.RummyGameService
import grails.plugin.springsecurity.SpringSecurityService
import grails.validation.Validateable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate

import java.security.Principal

class HomeController {

    SpringSecurityService springSecurityService
    EmailService emailService
    RummyGameService rummyGameService
    SimpMessagingTemplate brokerMessagingTemplate
    private static final String NEW_GAME_RESPONSE_TYPE = 'ngrt'
    Gson gson = new Gson()

    def index = {
        String userName = ((SecUser) springSecurityService.currentUser).username
        if (!session['userName']) {
            RummyGameUtil.addToLoggedInUsersTable(userName)
            brokerMessagingTemplate.convertAndSend "/topic/loggedIn", userName
            session['userName'] = userName
        }
        render view: 'home', model: getHomePageModel()
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
            render view: 'home', model: model
    }

    def beginGame (long gameId)  {
        String loggedInPlayer = session['userName']
        RummyGame game = rummyGameService.beginGame(gameId, loggedInPlayer)
        if ( game ) {
            flash.message = "Could not begin game. Either the game is already active or game is not started by you"
        }
        //re-render the home page to reflect the active games
        redirect(action: 'index')

    }
    //fetches all open games, invited games, active games, friends playerIDs details.
    //Pretty much all the data that is needed for home page
    private Map getHomePageModel() {
        String loggedInUser = session['userName']
        // fetch all the invited players across all games for currently logged in player. Required to display
        // active status of all friends to initiate chat with. Like facebook/google chat
        List<Player> currentPlayerFriends = rummyGameService.getAllPlayerIDsKnownTo(loggedInUser)?.collect {
            new Player(emailId: it, isLoggedInNow: RummyGameUtil.isUsedLoggedInNow(it))
        }
        Map modelMap = [currentPlayerFriends: currentPlayerFriends, loggedInUser: loggedInUser]
        List<RummyGame> gamesStartedByPlayer = RummyGame.findAllByOriginatorPlayerID(loggedInUser)
        if (gamesStartedByPlayer) {
            modelMap.put('gamesStartedByMe', gamesStartedByPlayer)
        }
        List<RummyGame> activeGames = (gamesStartedByPlayer?.findAll { it.isActive })?:[]
        List<RummyGameAssociatedPlayer> associatedGames = RummyGameAssociatedPlayer.findAllByPlayerId(loggedInUser)
        if (associatedGames) {
            List<RummyGame> gamesReceivedInvitation = associatedGames.findAll {
                it.gameAcceptStatus == GameAcceptStatus.NO_RESPONSE
            }.collect { it.game }
            if (gamesReceivedInvitation) {
                modelMap.put('openInvitations', gamesReceivedInvitation)
                gamesReceivedInvitation.each { it.isActive && activeGames << it }
            }
            List<RummyGame> gamesParticipated = associatedGames.findAll {
                it.gameAcceptStatus != GameAcceptStatus.NO_RESPONSE
            }.collect { it.game }
            if (gamesParticipated) {
                modelMap.put('participatedGames', gamesParticipated)
                gamesParticipated.each { it.isActive && activeGames << it }
            }
        }
        modelMap.put( 'activeGames', activeGames )
        return modelMap
    }

    @MessageMapping("/newGame/response")
    protected void newGameUserResponse(NewGameResponseVO newGameResponse, Principal principal) {
        if (rummyGameService.handleNewGameResponse(principal.getName(), newGameResponse)) {
            GenericTopicVO genericTopicVO = new GenericTopicVO()
            genericTopicVO.messages <<
                    new NotificationVO(type: NEW_GAME_RESPONSE_TYPE, message: newGameResponse.selectedAction,
                            gameId: newGameResponse.id, gameName: newGameResponse.gameName, fromUser: principal.getName())
            brokerMessagingTemplate.convertAndSend "/topic/general", gson.toJson(genericTopicVO)
        }
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
