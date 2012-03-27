/**
 * @author Ebot Tabi <ebot.tabi@gmail.com>
 * @copyright 2012, Ebot Tabi
 */
package controllers;

import play.*;
import play.libs.Codec;
import play.libs.Crypto;
import play.mvc.*;
import play.data.validation.*;
import play.data.validation.Error;
import java.util.*;
import models.User;
import notifiers.Notifier;
import com.google.gson.JsonObject;
import play.libs.OAuth2;
import play.libs.WS;

public class Session extends Controller {
	
    public static OAuth2 FACEBOOK = new OAuth2(
            "https://graph.facebook.com/oauth/authorize",
            "https://graph.facebook.com/oauth/access_token",
            "95341411595",
            "8eff1b488da7fe3426f9ecaf8de1ba54"
    );

	public static void index() {
		render();
	}

	public static void __login() {
		render();
	}

	public static void __dologin(
			@Required String login,
			@Required @MinSize(6) String password) {
		// Handle errors
		if (validation.hasErrors()) {
			render("@__login");
		}
		
		if (User.isEmail(login)) {
			
			if(User.count("password = ? and email = ?", Codec.hexSHA1(password), login) > 0){
				User user = User.findByEmail(login);
				//todo
			}else{
				flash.error("Your Email and Password combination incorrect ");
				render("@__login");
			}
					
		} else {
			
			if(User.count("password = ? and username = ?", Codec.hexSHA1(password), login) > 0){
				User user = User.findByEmail(login);
				//todo
			}else{
				flash.error("Your Username and Password combination incorrect ");
				render("@__login");
			}
			
		}

	}

	public static void __register() {
		render();
	}

	public static void __doregister(@Required String username,
			@Required @Email String email,
			@Required @MinSize(6) String password,
			@Required @Equals("password") String passwordConfirm) {
		// Handle errors
		if (validation.hasErrors()) {
			render("@__register");
		}
		if (User.findByEmail(email) != null) {
			flash.error("Email entered not available, please get another one");
			__register();
		}
		if (User.findByUsername(username) != null) {
			flash.error("Username entered not available, please get another one");
			__register();
		}
		User user = new User(username, password, email);
		try {
			Notifier.welcome(user);
			flash.success("Your account is created. Please check your emails ...");
			__register();

		} catch (Exception e) {
			Logger.error(e, "Mail error");
		}
	}
	
	

	public static void __forgot_password() {
		render();
	}
	
	public static void __doforgot_password(@Required String login) {
		// Handle errors
		if (validation.hasErrors()) {
			render("@__forgot_password");
		}
		if (User.isEmail(login)) {
			User user = User.findByEmail(login);
			notFoundIfNull(user);
			try {
				Notifier.resetPassword(user);
				flash.success("Please check your email, we have sent you instructions to reset your password");
				__forgot_password();

			} catch (Exception e) {
				Logger.error(e, "Mail error");
			}

		} else {
			User user = User.findByUsername(login);
			notFoundIfNull(user);
			try {
				Notifier.resetPassword(user);
				flash.success("Please check your email, we have sent you instructions to reset your password");
				__forgot_password();
			} catch (Exception e) {
				Logger.error(e, "Mail error");
			}
		}
		
	}

	

	public static void confirmRegistration(String token) {
		User user = User.findByRegistrationUUID(token);
		notFoundIfNull(user);
		user.needConfirmation = null;
		user.save();
		flash.success("Your account has been confirmed, you can now login");
		__login();
	}

	public static void reset(String token) {
		render(token);
	}
	
	public static void __doreset(
			String token,
			@Required @MinSize(6) String password,
			@Required @Equals("password") String passwordConfirm) {
		// Handle errors
		if (validation.hasErrors()) {
			render("@reset", token);
		}
		String email = User.decryptEmail(token);
		User user = User.findByEmail(email);
		user.password = Codec.hexSHA1(password);
		try {
			user.save();
			flash.success("You can now login with your new password!");
			__login();
			//normally we will email the user about the new password
		} catch (Exception e) {
			Logger.error(e, "Oops! still try to figure out");
		}
		
	}
	
	public static void twitter(){
		//ToDo for twitter connect
	}
	
	public static void facebook(){
	 if (OAuth2.isCodeResponse()) {       
            OAuth2.Response response = FACEBOOK.retrieveAccessToken(authURL());
            String access_token = response.accessToken;
            System.out.println(access_token);
        }
        FACEBOOK.retrieveVerificationCode(authURL());
	}
	
	static String authURL() {
        return play.mvc.Router.getFullUrl("Session.facebook");
    }

}