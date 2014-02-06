import grails.plugins.springsecurity.SpringSecurityService

class HomeController {

    SpringSecurityService springSecurityService

    def index = {
        [user: springSecurityService.currentUser ]
    }

}
