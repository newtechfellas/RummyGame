package com.webi.rummy.game.service

import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailService {
    static Properties props =
            ["mail.smtp.starttls.enable": "true",
                    "mail.smtp.auth": "true",
                    "mail.smtp.host": "smtp.gmail.com",
                    "mail.smtp.port": "587"] as Properties
    public void sendGameInviteEmail(String inviterEmailId, List<String> inviteesMailIds, String gameName ) {
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                //Get UserName and Password from DB to hide it when we upload the code to GITHUB
                //For testing you can use your gmailId and password here
                return new javax.mail.PasswordAuthentication(System.getenv('MAILER_ID'), System.getenv('MAILER_PASSWORD'));
            }
        });

        Message msg = new MimeMessage(session);
        //TODO: prefer groovy templates
        String GAME_INVITE_MAIL_BODY = """
                    Welcome To The Fun  World


                    You have been invited to play Rummy Card Game:$gameName by $inviterEmailId.
                    Please launch http://localhost:8080/home to view the invitation
                    """
        msg.setFrom(new InternetAddress(inviterEmailId, "Webi.com Admin"));
        InternetAddress[] inviteesInternetAddress =
                inviteesMailIds.
                        findAll { it =~/@gmail.com/ }.
                        collect { String emailId -> new InternetAddress(emailId, "Hello Friend") } as InternetAddress[]
        msg.addRecipients(Message.RecipientType.TO, inviteesInternetAddress);
        msg.setSubject("Rummy Card Game:$gameName Invitation");
        msg.setText(GAME_INVITE_MAIL_BODY);
        Transport.send(msg);
    }
}
