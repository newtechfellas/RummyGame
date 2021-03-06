package app

class SecUser {

    transient springSecurityService

    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    Date registeredDate
    String providerType

    static constraints = {
        username blank: false, unique: true
        password nullable: true
    }

    static mapping = {
        password column: '`password`'
    }

    Set<SecRole> getAuthorities() {
        SecUserRole.findAllByUser(this).collect { it.role } as Set
    }

    def beforeInsert() {
        if (password) {
            encodePassword()
        } else {
            password = 'NO_PASSWORD'
        }
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }

    String toString() {
        username
    }
}
