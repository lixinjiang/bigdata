package cn.lxj.bigdata.log.logMonitor.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * MailAuthenticator
 * description
 * create class by lxj 2019/1/31
 **/
public class MailAuthenticator extends Authenticator {
    String userName;
    String userPassword;

    public MailAuthenticator() {
        super();
    }

    public MailAuthenticator(String user, String pwd) {
        super();
        userName = user;
        userPassword = pwd;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, userPassword);
    }
}