/**
 * @author Ebot Tabi <ebot.tabi@gmail.com>
 * Copyright 2012, Ebot Tabi
 */
package models;

import javax.persistence.Entity;
import play.data.validation.*;
import play.db.jpa.Model;
import play.libs.Codec;
import play.libs.Crypto;
import utils.EmailValidation;
@Entity
public class User extends Model {
	
    
    @Required
    @Email
    @Unique
    public String email;
    
    @Required
    @Unique
    public String username;
    
    @Required
    @MinSize(6)
    public String password;
    
    @IsTrue
    public boolean nofityLike;
    
    @IsTrue
    public boolean notifyFollow;
    
    @IsTrue
    public boolean notifyComment;
    
    @IsTrue
    public boolean facebookUpdate;
    
    @IsTrue
    public boolean twitterUpdate;
    
    @IsTrue
    public boolean foursquareUpdate;
    
    @IsTrue
    public boolean notifyNewsUpdate;
    
    @Range(min=0, max=120)
    public String name;
    
    @Range(min=0, max=120)
    public String location;
    
    @MaxSize(140)
    public String bio;   
    
    @Range(min=0, max=120)
    public String website;
    
    public String twitterKey;
    
    public String twitterSecret;
    
    public String facebookKey;
    
    public String facebookSecret;
    
    public String foursquareKey;
    
    public String foursquareSecret;
    
    public String needConfirmation;
    
    public User(String username, String password, String email){
        this.username=username;
        this.password=Codec.hexSHA1(password);
        this.email= email;
        this.notifyNewsUpdate=true;
        this.notifyComment=true;
        this.notifyFollow=true;
        this.nofityLike=true;
        this.needConfirmation = Codec.UUID();
        create();
    }
    
    
    public static User connectByUsername(String username, String password) {
		return User.find("byUsernameAndPassword", username, Codec.hexSHA1(password)).first();
	}
    
    public static User findByUsername(String username){
    	return User.find("username", username).first();
    }
    
    public static User findByEmail(String email) {
        return find("email", email).first();
    }
    
    public static User findByRegistrationUUID(String uuid) {
        return find("needConfirmation", uuid).first();
    }
    
    public static boolean isEmail(String email){
    	EmailValidation check = new EmailValidation();
    	return check.validate(email);
    }
    
    public static String encryptEmail(String email){
    	User user = User.findByEmail(email);
    	return Crypto.encryptAES(user.email);
    }
    
    public static String decryptEmail(String token){
    	String email = Crypto.decryptAES(token);
    	User user = User.findByEmail(email);
    	return user.email;
    }
    
    public static  User connectByEmail(String email, String password) {
		return User.find("byEmailAndPassword",email, Codec.hexSHA1(password)).first();
	}
    

}
