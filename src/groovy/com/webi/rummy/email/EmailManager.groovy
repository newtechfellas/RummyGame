package com.webi.rummy.email
import com.webi.games.rummy.entity.MailManagerEntity
import groovy.transform.CompileStatic

import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
/**
 * Created by Suman Jakkula on 2/4/14.
 * San Antonio, Texas
 */
@CompileStatic
class EmailManager {
    static Properties props =
            ["mail.smtp.starttls.enable": "true",
                    "mail.smtp.auth": "true",
                    "mail.smtp.host": "smtp.gmail.com",
                    "mail.smtp.port": "587"] as Properties

    public static void sendGameInviteEmail(String inviterEmailId, String[] inviteesMailIds, long gameId,
                                           final MailManagerEntity mailManagerEntity) {
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                //Get UserName and Password from DB to hide it when we upload the code to GITHUB
                //For testing you can use your gmailId and password here
                return new javax.mail.PasswordAuthentication(mailManagerEntity.userId, mailManagerEntity.password);
            }
        });

        Message msg = new MimeMessage(session);
        //TODO: prefer groovy templates
        String GAME_INVITE_MAIL_BODY = """
                    Welcome To The Fun  World


                    You have been invited to play Rummy Card Game by $inviterEmailId.
                    Please launch http://localhost:8080/games/rummy/home to view the invitation
                    """
        msg.setFrom(new InternetAddress(inviterEmailId, "Webi.com Admin"));
        boolean validEmailExists = false;
       InternetAddress[] inviteesInternetAddress =
               inviteesMailIds.
               findAll { it =~/@gmail.com/ }.
               collect { String emailId -> new InternetAddress(emailId, "Hello Friend") } as InternetAddress[]
        msg.addRecipients(Message.RecipientType.TO, inviteesInternetAddress);
        msg.setSubject("Rummy Card Game Invitation");
        msg.setText(GAME_INVITE_MAIL_BODY);
        Transport.send(msg);
    }
}
