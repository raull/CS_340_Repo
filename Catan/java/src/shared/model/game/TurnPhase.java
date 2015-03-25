package shared.model.game;

import com.google.gson.annotations.SerializedName;

/**
 * The phase of the turn represented by the enumeration: 
 * ROLLING, 
 * ROBBING, 
 * PLAYING, 
 * DISCARDING,
 * FIRSTROUND, 
 * SECONDROUND
 * @author Raul Lopez
 *
 */
public enum TurnPhase {
	
	@SerializedName("Rolling")
	ROLLING,
	@SerializedName("Robbing")
	ROBBING,
	@SerializedName("Playing")
	PLAYING,
	@SerializedName("Discarding")
	DISCARDING,
	@SerializedName("FirstRound")
	FIRSTROUND,
	@SerializedName("SecondRound")
	SECONDROUND;
	
	public String original() {
		String original = "";
		switch(this) {
			case ROLLING:
				original = "Rolling";
				break;
			case ROBBING:
				original = "Robbing";
				break;
			case PLAYING:
				original = "Playing";
				break;
			case DISCARDING:
				original = "Discarding";
				break;
			case FIRSTROUND:
				original = "FirstRound";
				break;
			case SECONDROUND:
				original = "SecondRound";
				break;
		}
		return original;
	}
	
}
