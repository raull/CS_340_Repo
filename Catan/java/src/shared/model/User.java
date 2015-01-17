package shared.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import shared.definitions.CatanColor;
import shared.definitions.DevCardType;

/**
 * This class represent a user for playing games and authenticating as well
 * @author Raul Lopez
 *
 */
public class User {

	/**
	 * Color that represents the <code>User</code> described by {@link CatanColor}
	 */
	private CatanColor color;
	/**
	 * A {@link String} representing the name of the <code>User</code>
	 */
	private String name;
	/**
	 * A {@link String} representing the password of the <code>User</code>
	 */
	private String password;
	/**
	 * A <code>List</code> representing the cards of the <code>User</code>
	 */
	private ArrayList<DevCardType> cards;
	
	/**
	 * Constructor to instantiate a <code>User</code> with authentication information and a color
	 * @param A {@link String} representing the name of the <code>User</code>
	 * @param A {@link String} representing the password of the <code>User</code>
	 * @param A {@link CatanColor} representing the color of the <code>User</code>
	 */
	public User(String name, String password, CatanColor color) {
		this.name = name;
		this.color = color;
	}
	
	//Getters
	
	public String getName() {
		return this.name;
	}
	
	public Color getColor() {
		return color.getJavaColor();
	}
	
	public Collection<DevCardType> getDevCards() {
		return Collections.unmodifiableCollection(this.cards);
	}

	public String getPassword() {
		return password;
	}

	
}
