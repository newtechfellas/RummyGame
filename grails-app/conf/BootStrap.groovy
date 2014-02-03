import app.SecRole
import app.SecUser

class BootStrap {

    def init = { servletContext ->

        if( !SecUser.findByUsername('suman.jakkula@gmail.com') ){
            new SecUser(username: 'suman.jakkula@gmail.com', password: 'password', favoriteColor: 'Blue').save()
            new SecRole(authority: 'ROLE_USER').save(failOnError: true, flush: true)
            new SecRole(authority: 'ROLE_FACEBOOK').save(failOnError: true, flush: true)
            new SecRole(authority: 'ROLE_EXAMPLE').save(failOnError: true, flush: true)
        }

    }
    def destroy = {
    }
}
