package app

class FacebookUser {

    long uid
    String accessToken
    Date accessTokenExpires

    static belongsTo = [user: SecUser] //connected to main Spring Security domain

    static constraints = { uid unique: true }
}
