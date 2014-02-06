package com.webi.games.rummy.entity

/**
 * Created by Suman Jakkula on 2/4/14.
 * San Antonio, Texas
 */
class MailManagerEntity {
    String userId
    String password

    static constraints = {
        id name:'userId', maxSize: 200
        password maxSize: 50
    }
}
