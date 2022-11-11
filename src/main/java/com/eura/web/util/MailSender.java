package com.eura.web.util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailSender {
    @Value("${mail.passwd}")
    private String mailpasswd;

    private String FROM = "admin@eura.site";
    private String FROMNAME = "EURA";
    private String SMTP_USERNAME = "admin@eura.site";
    private String HOST = "smtps.hiworks.com";
    private int PORT = 465;

    public void sender(String To, String Subject, String Body) throws Exception {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT); 
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
        props.put("mail.smtp.ssl.trust", HOST);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
         
        Session session = Session.getDefaultInstance(props);
        // session.setDebug(true); //for debug
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(To));
        msg.setSubject(Subject);
        msg.setContent(Body, "text/html;charset=utf-8");
        
        Transport transport = session.getTransport();
        try {
            // System.out.println("Sending...");
            transport.connect(HOST, SMTP_USERNAME, mailpasswd);
            transport.sendMessage(msg, msg.getAllRecipients());
            // System.out.println("Email sent!");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            transport.close();
        }
    }
}
