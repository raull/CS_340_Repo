package shared.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import shared.definitions.CatanColor;
import shared.definitions.DevCardType;

public class Player {

	private CatanColor color;
	private String name;
	private ArrayList<DevCardType> cards;
	
	public Player(String name, CatanColor color) {
		this.name = name;
		this.color = color;
	}
	
	//Getters and Setters
	
	public String getName() {
		return this.name;
	}
	
	public Color getColor() {
		return color.getJavaColor();
	}
	
	public Collection<DevCardType> getDevCards() {
		return Collections.unmodifiableCollection(this.cards);
	}
	
}
