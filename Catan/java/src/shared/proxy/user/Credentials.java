package shared.proxy.user;

import shared.model.game.*;
/**
 * Contains a username and password. Used for communication via
 * Proxy
 * @author Kent
 *
 */
public class Credentials {

	/** 
	 * The username of the <code>User</code>, must be between 3 and 7
	 * characters.
	 */
	private String username;
	
	/**
	 * The password of the <code>User<code>, which is 5 or more characters
	 * that are alphanumeric, underscores, or hyphens.
	 */
	private String password;

	/**
	 * Constructor to instantiate <code>Credentials</code> with a username and password
	 * @param username
	 * @param password
	 */
	public Credentials(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	/**
	 * Extracts the username and password from a User.
	 * @param user
	 */
	public Credentials(User user){
		//CODE
	}
	public String getUsername() {
		return username;
	}

	/**
	 * If the username is valid, sets it.
	 * @param username
	 */
	public void setUsername(String username) {
		//VERIFICATION CODE
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * If valid, sets the password.
	 * @param password
	 */
	public void setPassword(String password) {
		//VERIFICATION CODE
		this.password = password;
	}
	
	

}
