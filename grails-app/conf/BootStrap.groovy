import app.SecRole
import app.SecUser
import com.webi.games.rummy.entity.MailManagerEntity

class BootStrap {

    def init = { servletContext ->
        if( !SecUser.findByUsername('suman.jakkula@gmail.com') ){
            new SecRole(authority: 'ROLE_USER').save(failOnError: true, flush: true)
            new SecRole(authority: 'ROLE_FACEBOOK').save(failOnError: true, flush: true)
            new SecRole(authority: 'ROLE_EXAMPLE').save(failOnError: true, flush: true)
        }
        new MailManagerEntity(userId: System.getenv('MAILER_ID'), password: System.getenv('MAILER_PASSWORD')).save(failOnError: true, flush: true)
    }

    def destroy = {

    }
}
