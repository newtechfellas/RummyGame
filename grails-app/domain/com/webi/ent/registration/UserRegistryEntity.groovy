package com.webi.ent.registration

class UserRegistryEntity {
    String userId;
    String password;
    String userName;
    Date registerDate;
    Date unRegisterDate;
    
    static constraints = {
        id name:'userId'
        unRegisterDate nullable: true
    }
}
