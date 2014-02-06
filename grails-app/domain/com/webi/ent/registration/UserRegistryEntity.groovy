package com.webi.ent.registration

class UserRegistryEntity {
    String email
    String name
    String password ='password'; //default to auto-create users who logon with oAuth
    String providerType
    Date registerDate;
    Date unRegisterDate;
    
    static constraints = {
        id name:'email'
        unRegisterDate nullable: true
    }
}
