package utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class EmailValidation {
	  private Pattern pattern;
	  private Matcher matcher;

	  private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	  public EmailValidation(){
		  pattern = Pattern.compile(EMAIL_PATTERN);
	  }

	  /**
	   * Validate email with regular expression
	   * @param email email for validation
	   * @return true valid or false invalid
	   */
	  public boolean validate(final String email){
		  matcher = pattern.matcher(email);
		  boolean  matchFound = matcher.matches();
		  if (matchFound){
			  return true;
		  }else{
			  return false;
		  }

	  }
	  
	
	
}
