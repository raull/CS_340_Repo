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
	@SerializedName("SecondROund")
	SECONDROUND
	
}
