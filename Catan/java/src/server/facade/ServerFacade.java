package server.facade;

import java.util.List;

import server.exception.ServerInvalidRequestException;
import server.game.Game;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.Model;
import shared.model.cards.ResourceCardDeck;

public class ServerFacade {

	/**
	 * Validates the player's credentials, and logs them in to the server.
	 * @param username The user's username (case-sensitive)
	 * @param password The user's password (case-sensitive)
	 * @throws ServerInvalidRequestException 
	 */
	public void login(String username, String password) throws ServerInvalidRequestException {
		
	}
	
	/**
	 * Creates a new player account, and logs them in to the server
	 * @param username The user's username (case-sensitive)
	 * @param password The user's password (case-sensitive)
	 * @throws ServerInvalidRequestException
	 */
	public void register(String username, String password) throws ServerInvalidRequestException {
		
	}
	
	/**
	 * Get a list of all games in progress.
	 * @return The list of games currently in progress
	 * @throws ServerInvalidRequestException
	 */
	public List<Game> gameList() throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Creates and returns a new game.
	 * @param name The name of the new game.
	 * @param randomTiles Boolean describing if the new game should have random tile locations.
	 * @param randomNumbers Boolean describing if the new game should have the numbers randomly allocated on the map.
	 * @param randomPorts Boolean describing if the location of the ports should be random on the new game.
	 * @return A newly created game.
	 * @throws ServerInvalidRequestException
	 */
	public Game createNewGame(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Adds (or re-adds) the player to the specified game
	 * @param gameId The ID of the game to join
	 * @param color The color of the player for the game to join. Should not be taken by another player already.
	 * @throws ServerInvalidRequestException
	 */
	public void joinGame(int gameId, String color) throws ServerInvalidRequestException {
		
	}
	
	/**
	 * This method is for testing and debugging purposes. 
	 * When a bug is found, you can use the /games/save method to save 
	 * the state of the game to a file, and attach the file to a bug report. 
	 * A developer can later restore the state of the game when the bug 
	 * occurred by loading the previously saved file using the /games/load method. 
	 * Game files are saved to and loaded from the server's saves/ directory.
	 * @param gameId The ID of the game to save.
	 * @param fileName The file name you want to save it under
	 * @throws ServerInvalidRequestException
	 */
	public void gameSave(int gameId, String fileName) throws ServerInvalidRequestException {
		
	}
	
	/**
	 * This method is for testing and debugging purposes. When a bug is found, 
	 * you can use the /games/save method to save the state of the game to a file, 
	 * and attach the file to a bug report. A developer can later restore the state 
	 * of the game when the bug occurred by loading the previously saved file using 
	 * the /games/load method. Game files are saved to and loaded from the server's 
	 * saves/ directory.
	 * @param fileName The name of the saved game file that you want to load. (The game's ID is restored as well.)
	 * @return The game that was previously saved.
	 * @throws ServerInvalidRequestException
	 */
	public Game gameLoad(String fileName) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Return the model
	 * @param version The version number of the model that the caller already has. 
	 * It goes up by one for each command that is applied. If you send this parameter, 
	 * you will get a model back only if the current model is newer than the specified 
	 * version number. Otherwise, it returns the string "true" to notify the caller that 
	 * it already has the current model state.
	 * @param game The game from which the model comes from.
	 * @return
	 * @throws ServerInvalidRequestException
	 */
	public Model getModel(int version, Game game) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * For the default games created by the server, this method reverts the game to the 
	 * state immediately after the initial placement round. For user-created games, this 
	 * method reverts the game to the very beginning (i.e., before the initial placement 
	 * round). This method returns the client model JSON for the game after it has been 
	 * reset.
	 * @param game The game to reset.
	 * @throws ServerInvalidRequestException
	 */
	public Model resetGame(Game game) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Sends a chat message
	 * @param gameThe game were the chat belongs to
	 * @param playerIndexThe index of the sender
	 * @param message The message of the chat
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public Model sendChat(Game game, int playerIndex, String message) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Used to roll a number at the beginning of your turn
	 * @param game The game where the dice is rolled
	 * @param playerIndex The index of the player who rolled
	 * @param rolledNumber The number of the dice
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public Model rollNumber(Game game, int playerIndex, int rolledNumber) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Moves the robber, selecting the new robber position and player to rob.
	 * @param game The game where the robbing is taking place.
	 * @param playerIndex The index of the player robbing.
	 * @param victimIndex The index of the player being robbed.
	 * @param location The Location where the robber will be placed.
	 * @param soldierCard Whether or not the robbing took place with a soldier card or not
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public Model robPlayer(Game game, int playerIndex, int victimIndex, HexLocation location, boolean soldierCard) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Used to finish a player's turn.
	 * @param game The game were its taking place.
	 * @param playerIndex The index of the player ending the turn.
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public Model finishTurn(Game game, int playerIndex) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Used for a player to buy a development card
	 * @param game The game where the transaction is being made.
	 * @param playerIndex THe player doing the transaction
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public Model buyDevCard(Game game, int playerIndex) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Plays a 'Year of Plenty' card from your hand to gain the two specified resources.
	 * @param game THe game where the card is being played.
	 * @param playerIndex The index of the player playing the card.
	 * @param resource1 The first resource to gain.
	 * @param resource2 The second resource to gain.
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public Model playYearOfPlenty(Game game, int playerIndex, ResourceType resource1, ResourceType resource2) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Plays a 'Road Building' card from your hand to build two roads at the specified locations
	 * @param game The game where the card is being played.
	 * @param playerIndex The index of the player playing the card.
	 * @param location1 The location of the first road
	 * @param location2 The location of the second road
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public Model playRoadBuilding(Game game, int playerIndex, EdgeLocation location1, EdgeLocation location2) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Plays a 'Monopoly' card from your hand to monopolize the specified resource.
	 * @param game The game where the card will be played.
	 * @param playerIndex The index of the player who is playing the card.
	 * @param resource The resource type to get all the resources
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public Model playMonopoly(Game game, int playerIndex, ResourceType resource) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Plays a 'Monument' card from your hand to give you a victory point.
	 * @param game The game where the card will be played
	 * @param playerIndex The index of the player who is playing the card.
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public Model playMonument(Game game, int playerIndex) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Builds a road at the specified location. (Set 'free' to true during initial setup.)
	 * @param game The game where the road will be placed
	 * @param playerIndex The index of the player who is placing the road.
	 * @param roadLocation The location where the road will be placed.
	 * @param free Whether or not the road is build for free
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public Model buildRoad(Game game, int playerIndex, EdgeLocation roadLocation, boolean free) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Builds a settlement at the specified location. (Set 'free' to true during initial setup.)
	 * @param game The game where the settlement will be placed
	 * @param playerIndex The index of the player who is placing the settlement.
	 * @param vertexLocation The vertex location where the settlement will be placed.
	 * @param free Whether or not the settlement will be placed for free.
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public Model buildSettlement(Game game, int playerIndex, VertexLocation vertexLocation, boolean free) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Builds a city at the specified location.
	 * @param game The game where the city will be placed.
	 * @param playerIndex The index of the player who is placing the city.
	 * @param vertexLocation THe location where the city will be placed.
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public Model buildCity(Game game, int playerIndex, VertexLocation vertexLocation) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Offers a domestic trade to another player.
	 * @param game The game where the offer will be made
	 * @param playerIndex The index of the player placing the offer.
	 * @param receiver The index of the player receiving the offer.
	 * @param senderDeck The deck of resources that the sender is giving.
	 * @param receiverDeck the deck of resources that the receiver is giving in return.
	 * @return Returns the client model (identical to getModel).
	 * @throws ServerInvalidRequestException
	 */
	public Model offerTrade(Game game, int playerIndex, int receiver, ResourceCardDeck senderDeck, ResourceCardDeck receiverDeck) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Used to accept or reject a trade offered to you.
	 * @param game The game where the trade was made
	 * @param playerIndex The index of the player rejecting/accepting the trade.
	 * @param accept Whether or not the player is accepting the trade.
	 * @return Returns the client model (identical to getModel).
	 * @throws ServerInvalidRequestException
	 */
	public Model acceptTrade(Game game, int playerIndex, boolean accept) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Used to execute a maritime trade.
	 * @param game The game where the trade will be made.
	 * @param playerIndex The index of the player making the offer.
	 * @param ratio The ratio of the trading (Ex. 3:1, 2:1, 4:1).
	 * @param sendingResource The resource type being offered.
	 * @param receivingResource The resource type being received.
	 * @return Returns the client model (identical to getModel).
	 * @throws ServerInvalidRequestException
	 */
	public Model maritimeTrade(Game game, int playerIndex, int ratio, ResourceType sendingResource, ResourceType receivingResource) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Discards the specified resource cards.
	 * @param game The game where the cards will be discarded.
	 * @param playerIndex The index of the player discarding cards.
	 * @param resourcesToDiscard The cards to be discarded.
	 * @return Returns the client model (identical to getModel).
	 * @throws ServerInvalidRequestException
	 */
	public Model discardCards(Game game, int playerIndex, ResourceCardDeck resourcesToDiscard) throws ServerInvalidRequestException {
		return null;
	}
	
}
