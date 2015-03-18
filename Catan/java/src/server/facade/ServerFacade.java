package server.facade;

import java.util.List;

import com.google.gson.JsonElement;

import server.exception.ServerInvalidRequestException;
import server.game.Game;
import server.game.GameManager;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.Model;
import shared.model.cards.ResourceCardDeck;
import shared.model.facade.ModelFacade;
import shared.model.game.MessageLine;
import shared.model.game.MessageList;
import shared.model.game.User;

/**
 * Facade of the server to make all operations to an specific game.
 * @author raulvillalpando
 *
 */
public class ServerFacade {
	
	private static ServerFacade instance;
	private GameManager gameManager = new GameManager();
	
	private ServerFacade() {
		
	}
	
	/**
	 * Singleton instance of the Server Facade
	 * @return
	 */
	public ServerFacade instance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new ServerFacade();
			return instance;
		}
	}

	/**
	 * Validates the player's credentials, and logs them in to the server.
	 * @param username The user's username (case-sensitive)
	 * @param password The user's password (case-sensitive)
	 * @throws ServerInvalidRequestException 
	 */
	public void login(String username, String password) throws ServerInvalidRequestException 
	{
		//if a user does not exist in the user manager with the given name and password
			//throw exception
		//else the user cookie needs to be set for the client (done in handlers?)
	}
	
	/**
	 * Creates a new player account, and logs them in to the server
	 * @param username The user's username (case-sensitive)
	 * @param password The user's password (case-sensitive)
	 * @throws ServerInvalidRequestException
	 */
	public void register(String username, String password) throws ServerInvalidRequestException 
	{
		//if a user already exists in the usermanager with the given name and password
			//throw exception
		//else
			//create a new user in the user manager
			//set the user cookie for the client (done in handlers?)
		
		//do we also need to verify on the server side that the username and password are valid (size, characters, etc)?
	}
	
	/**
	 * Get a list of all games in progress.
	 * @return The list of games currently in progress
	 * @throws ServerInvalidRequestException
	 */
	public List<Game> gameList() throws ServerInvalidRequestException 
	{
		//gets the list of games from the game manager
		//at some point we need to be creating a specific JSON element here
		//is that going to be done in the handler?
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
	public JsonElement createNewGame(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts) throws ServerInvalidRequestException 
	{
		//have a hard coded list of default tiles, numbers, and ports?
		
		//don't have to account for automatically adding the player to the game here
		//that is done client side
		
		//this function should also save the beginning state of the map somewhere
		//This way if the reset function is called the model can update using this saved file
		//This also means a Game object should also store the string representing the filename of the intial setup
		
		return null;
	}
	
	/**
	 * Adds (or re-adds) the player to the specified game
	 * @param gameId The ID of the game to join
	 * @param color The color of the player for the game to join. Should not be taken by another player already.
	 * @throws ServerInvalidRequestException
	 */
	public void joinGame(int gameId, String color) throws ServerInvalidRequestException 
	{
		
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
	public JsonElement gameLoad(String fileName) throws ServerInvalidRequestException {
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
	public JsonElement getModel(int version, int gameId) throws ServerInvalidRequestException {
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
	public JsonElement resetGame(int gameId) throws ServerInvalidRequestException {
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
	public JsonElement sendChat(int gameId, int playerIndex, String message) throws ServerInvalidRequestException 
	{
		//access objects of the specific game
		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		Model model = modelFacade.getModel();
		MessageList chat = model.getChat();
		
		User user = modelFacade.turnManager().getUserFromIndex(playerIndex);
		
		//add the new chat message to the list of chats
		chat.addMessage(new MessageLine(message, user.getName()));
		
		return getModel(0, gameId); //new version of the model
	}
	
	/**
	 * Used to roll a number at the beginning of your turn
	 * @param game The game where the dice is rolled
	 * @param playerIndex The index of the player who rolled
	 * @param rolledNumber The number of the dice
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */

	public JsonElement rollNumber(int gameId, int playerIndex, int rolledNumber) throws ServerInvalidRequestException {

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
	public JsonElement robPlayer(int gameId, int playerIndex, int victimIndex, HexLocation location, boolean soldierCard) throws ServerInvalidRequestException 
	{		
		//if can robPlayer
			//move the robber to the new location
			//randomly select a resource card from the victim
			//remove it from the victim and add it to the given player
			//if soldier card
				//remove a soldier devcard from the player
				//add one to the number of soldiers the player has played
				//check to see if they gained the largest army
				//if so
					//set largest army
					//update points (may involve taking points away from another player)
				//update game log with appropriate soldier message(s)
			//else
				//update game log with appropriate robbing message
			//set the turn phase to now be playing
		//else (can't rob player)
			//throw exception
		
		//return new model
		
		return null;
	}
	
	/**
	 * Used to finish a player's turn.
	 * @param game The game were its taking place.
	 * @param playerIndex The index of the player ending the turn.
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public JsonElement finishTurn(int gameId, int playerIndex) throws ServerInvalidRequestException 
	{
		//if can finish turn
			//update the current turn index to be the next index
				//this should be a separate function that needs to account for the special rules of the first and second rounds
			//if the player has any dev cards they bought this turn, move them to the old devcards
			//update the turn phase
				//could be updated to rolling, firstround, or second round
		//else
			//throw exception
		
		//return new model
		return null;
	}
	
	/**
	 * Used for a player to buy a development card
	 * @param game The game where the transaction is being made.
	 * @param playerIndex THe player doing the transaction
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public JsonElement buyDevCard(int gameId, int playerIndex) throws ServerInvalidRequestException 
	{
		//if can buy dev card
			//subtract 1 sheep, 1 wheat, and 1 ore from the player's resources
			//add the same resources to the resource bank
			//randomly add a dev card to the player's new dev cards from the dev card bank
			//update game history
		//else
			//throw exception
		
		//return new Model
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
	public JsonElement playYearOfPlenty(int gameId, int playerIndex, ResourceType resource1, ResourceType resource2) throws ServerInvalidRequestException 
	{
		//if can play dev card
			//add 1 of each of the specified resources to the player's resources
			//subtract the same resources from the resource bank
			//subtract one from the player's year of plenty cards
			//update game history
		//else
			//throw exception
		
		//return new model
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
	public JsonElement playRoadBuilding(int gameId, int playerIndex, EdgeLocation location1, EdgeLocation location2) throws ServerInvalidRequestException 
	{
		//if can play dev card
			//subtract 2 from the player's available roads
			//subtract 1 from the player's road building cards
			//give the player roads on the 2 given edges
			//check if longest road needs to be updated
			//update game history
		//else
			//throw exception
		
		//return new model
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
	public JsonElement playMonopoly(int gameId, int playerIndex, ResourceType resource) throws ServerInvalidRequestException 
	{
		//if can play dev card
			//count the total number of cards of the specific resource in any player's hand
			//set the player to have that number of that resource
			//set every other player to have 0 of that resource
			//subtract 1 from the player's monopoly cards
			//update game history
		//else
			//throw exception
		
		//return new Model
		return null;
	}
	
	/**
	 * Plays a 'Monument' card from your hand to give you a victory point.
	 * @param game The game where the card will be played
	 * @param playerIndex The index of the player who is playing the card.
	 * @return Returns the client model (identical to getModel)
	 * @throws ServerInvalidRequestException
	 */
	public JsonElement playMonument(int gameId, int playerIndex) throws ServerInvalidRequestException 
	{
		//if can play dev card
			//subtract one from the player's monument cards (check if new or old?)
			//add one to the player's points
			//update game history
		//else
			//throw exception
		
		//return new model
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
	public JsonElement buildRoad(int gameId, int playerIndex, EdgeLocation roadLocation, boolean free) throws ServerInvalidRequestException 
	{
		//if can buildroad
			//subtract 1 from the player's available roads
			//place a road on the given edge
			//if not free
				//subtract 1 wood and 1 brick from the player's resources
				//add those resources to the resource bank
			//check if player has gained longest road
			//update game history
		//else
			//throw exception
		
		//return new model
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
	public JsonElement buildSettlement(int gameId, int playerIndex, VertexLocation vertexLocation, boolean free) throws ServerInvalidRequestException 
	{
		//if can buildsettlement
			//subtract 1 from the player's available settlements
			//place a settlement on the given vertex
			//if not free
				//subtract 1 wood, 1 brick, 1 sheep, and 1 wheat from the player's resources
				//add those resources to the resource bank
			//add 1 to the player's points
			//update game history
		//else
			//throw exception
	
		//return new model
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
	public JsonElement buildCity(int gameId, int playerIndex, VertexLocation vertexLocation) throws ServerInvalidRequestException 
	{
		//if can buildcity
			//subtract 1 from the player's available cities
			//place a city on the given vertex
			//remove the settlement from the given vertex??
			//subtract 2 wheat and 3 ore from the player's resources
			//add those resources to the resource bank
			//add 1 to player's points
			//update game history
		//else
			//throw exception
	
		//return new model
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
	public JsonElement offerTrade(int gameId, int playerIndex, int receiver, ResourceCardDeck senderDeck, ResourceCardDeck receiverDeck) throws ServerInvalidRequestException {
		//if can offer trade
			//add a trade offer to model
		//else throw exception
		
		//return new model
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
	public JsonElement acceptTrade(int gameId, int playerIndex, boolean accept) throws ServerInvalidRequestException {
		//if user can accept trade
			//if user accepted trade
				//current player and player trading swap specified resources
			//else user rejected trade
		
		//else throw exception
		
		//trade offer is removed from model
		
		//return model
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
	public JsonElement maritimeTrade(int gameId, int playerIndex, int ratio, ResourceType sendingResource, ResourceType receivingResource) throws ServerInvalidRequestException {
		//get the game by id
		//if can maritime trade
			//subtract 1 of receiving resource type from bank
			//bank gets ratio number of sending resource type
			//user loses ratio number of sending resource type
			//user gains 1 of receiving resource type
		//else exception
		//return new model
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
	public JsonElement discardCards(int gameId, int playerIndex, ResourceCardDeck resourcesToDiscard) throws ServerInvalidRequestException 
	{
		//if can discard cards
			//subtract the resources in the given resource card deck from the player
			//add the same resources to the resource bank
			//if all players who needed to discard have now discarded (how are we going to determine this)
				//update turn phase to now be robbing
		//else
			//throw exception
		
		//return new model
		return null;
	}
	
	
	/////////////Getters and Setters/////////////////
	
	public GameManager getGameManager() {
		return gameManager;
	}
}
