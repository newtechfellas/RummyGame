import com.webi.ent.util.RummyGameUtil
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.messaging.simp.SimpMessagingTemplate

class LogoutController {

    SimpMessagingTemplate brokerMessagingTemplate
	/**
	 * Index action. Redirects to the Spring security logout uri.
	 */
	def index = {
        RummyGameUtil.removeFromLoggedInUsersTable(session['userName'])
        brokerMessagingTemplate.convertAndSend "/topic/loggedOut", session['userName']
		redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
	}
}
