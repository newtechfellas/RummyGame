import grails.plugins.springsecurity.SpringSecurityService

class HomeController {

    SpringSecurityService springSecurityService

    def index = {
        //Perhaps a bad choice to place user object in session for gsp access. Its ok for this app.
        session['user'] = springSecurityService.currentUser
    }

    def startNewGame = {
        StartNewGameCommand command ->
            if (command.validate()) {

            }
            render view: "index", model: [command: command]
    }


}

@grails.validation.Validateable
class StartNewGameCommand {
    String playerIds
    String gameName
    static constraints = {
        playerIds nullable: false, blank: false
        gameName nullable: false
    }
}
