package notifiers;

import play.mvc.*;

import javax.mail.internet.*;

import models.*;

public class Notifier extends Mailer {

    public static void welcome(User user) throws Exception {
        setFrom(new InternetAddress("noreply@picplx.com", "Picplx photography on the fly"));
        setSubject("Welcome %s", user.username);
        addRecipient(user.email);
        send(user);
    }
    
    public static void resetPassword(User user) throws Exception{
    	setFrom(new InternetAddress("noreply@picplx.com", "Password Reset"));
    	setSubject("Welcome %s", user.username);
        addRecipient(user.email);
        send(user);
    }
    
}

