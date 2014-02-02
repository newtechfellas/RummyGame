import app.SecUser

class BootStrap {

    def init = { servletContext ->

        if( !SecUser.findByUsername('bryan.chug@gmail.com') ){
            new SecUser(username: 'bryan.chug@gmail.com', password: 'password', favoriteColor: 'Blue').save()
        }

    }
    def destroy = {
    }
}
