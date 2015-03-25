package server.facade;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import server.exception.ServerInvalidRequestException;
import server.game.Game;
import server.game.GameManager;
import server.user.UserManager;

import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.PieceType;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.Model;
import shared.model.board.Edge;
import shared.model.board.piece.Road;
import shared.model.cards.DevCard;
import shared.model.cards.ResourceCard;
import shared.model.board.Vertex;
import shared.model.board.piece.Building;
import shared.model.cards.ResourceCardDeck;
import shared.model.game.User;
import shared.model.exception.ModelException;
import shared.model.facade.ModelFacade;
import shared.model.game.MessageLine;
import shared.model.game.MessageList;
import shared.model.game.TradeOffer;
import shared.model.game.TurnManager;
import shared.model.game.TurnPhase;
import shared.model.game.User;



/**
 * Facade of the server to make all operations to an specific game.
 * @author raulvillalpando
 *
 */
public class ServerFacade {
	
	private static ServerFacade instance;
	private GameManager gameManager = new GameManager();
	private UserManager userManager = new UserManager();
	
	private ServerFacade() {
		//Default games
		try {
			createNewGame("Empty Game", false, false, false);
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Singleton instance of the Server Facade
	 * @return
	 */
	public static ServerFacade instance() {
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
	public JsonElement login(String username, String password) throws ServerInvalidRequestException 
	{
		if (username == null){
			throw new ServerInvalidRequestException("Login Failed: Username field not found");
		}
		if (password == null){
			throw new ServerInvalidRequestException("Login Failed: Password field not found");
		}
		if (!userManager.userExists(username, password)){
			throw new ServerInvalidRequestException("Login Failed: invalid username or password");
		}
			
		User user = userManager.getUser(username);
		
		if (user == null) {
			throw new ServerInvalidRequestException("User cannot be found at the moment");
		} else {
			JsonObject response = new JsonObject();
			response.addProperty("id", user.getPlayerID());
			return response;
		}
	}
	
	/**
	 * Creates a new player account, and logs them in to the server
	 * @param username The user's username (case-sensitive)
	 * @param password The user's password (case-sensitive)
	 * @throws ServerInvalidRequestException
	 */
	public JsonElement register(String username, String password) throws ServerInvalidRequestException 
	{
		if (username == null){
			throw new ServerInvalidRequestException("Login Failed: Username field not found");
		}
		if (password == null){
			throw new ServerInvalidRequestException("Login Failed: Password field not found");
		}
		if (userManager.userExists(username, password)){
			throw new ServerInvalidRequestException("Register Failed: User already exists.");
		}
		if (username.length() < 3 || username.length() > 7){
			throw new ServerInvalidRequestException("Register Failed: "
					+ "Username must be between 3 and 7 characters.");
		}
		if (password.length() < 5){
			throw new ServerInvalidRequestException("Register Failed: Password not long enough");
		}
		
		for (int i = 0; i < password.length(); i++){
			Character ch = password.charAt(i);
			boolean isGood = false;
			if (Character.isAlphabetic(ch) || Character.isDigit(ch) || ch.equals('_') || ch.equals('-'))
				isGood = true;
			else{
				isGood = false;
				throw new ServerInvalidRequestException("Register Failed: Password contains invalid characters");
			}
		}
		
			User newUser = userManager.addNewUser(username, password);
			JsonObject response = new JsonObject();
			response.addProperty("id", newUser.getPlayerID());
			return response;
		
					
	}
	
	/**
	 * Get a list of all games in progress.
	 * @return The list of games currently in progress
	 * @throws ServerInvalidRequestException
	 */
	public JsonElement gameList() throws ServerInvalidRequestException 
	{
		List<Game> games = gameManager.getGames();
				
		JsonArray gamesJSON = new JsonArray();
		
		for (Game game : games) {
			gamesJSON.add(game.jsonRepresentation());
		}
		
		return gamesJSON;
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
	public JsonElement createNewGame(String name, Boolean randomTiles, Boolean randomNumbers, Boolean randomPorts) throws ServerInvalidRequestException 
	{
		if (name == null )
		{
			throw new ServerInvalidRequestException("The 'name' field was missing.");
		}
		else if (randomTiles == null)
		{
			throw new ServerInvalidRequestException("The 'randomTiles' field was missing.");

		}
		else if (randomNumbers == null)
		{
			throw new ServerInvalidRequestException("The 'randomNumbers' field was missing.");

		}
		else if (randomPorts == null)
		{
			throw new ServerInvalidRequestException("The 'randomPorts' field was missing.");

		}
		
		Boolean test = null;
		//have a hard coded list of default tiles, numbers, and ports?
		int newGameId = gameManager.getNextId();
		
		ModelFacade newFacade = new ModelFacade(randomTiles, randomNumbers, randomPorts);
		
		Game newGame = new Game(newGameId, name, newFacade);
		gameManager.addGame(newGame);
		
		
		//this function should also save the beginning state of the map somewhere
		//This way if the reset function is called the model can update using this saved file
		//This also means a Game object should also store the string representing the filename of the intial setup

		return newGame.jsonRepresentation();
	}
	
	/**
	 * Adds (or re-adds) the player to the specified game
	 * @param gameId The ID of the game to join
	 * @param color The color of the player for the game to join. Should not be taken by another player already.
	 * @throws ServerInvalidRequestException
	 */
	public JsonElement joinGame(int gameId, String color, int userID) throws ServerInvalidRequestException 
	{
		Game gameToJoin = gameManager.getGameById(gameId);
		
		if (gameToJoin == null) {
			throw new ServerInvalidRequestException("Cannot join. Game doesn't exist.");
		}
		
		ModelFacade facade = gameToJoin.getModelFacade();
		TurnManager tm = facade.turnManager();
		
		color = color.toUpperCase();
		CatanColor nuColor = CatanColor.valueOf(color);
		
		//Verify user
		User existingUser = userManager.getUser(userID);
		if (existingUser == null) {
			throw new ServerInvalidRequestException("Cannot join. User does not exist.");
		}
		existingUser = existingUser.clone();
		
		//Verify color	
		if (nuColor != null)
			existingUser.setColor(nuColor);
		else{
			throw new ServerInvalidRequestException("Cannot join. Select a valid color.");
		}
		
		for (User u : tm.getUsers()){
			if (u.getCatanColor().equals(nuColor) && !u.getName().equals(existingUser.getName())){ 
				throw new ServerInvalidRequestException("Cannot join. That color has been chosen.");
			}
		}
		
		//Checks to see if we can add player
		if (tm.getUsers().size() < 4){ //enough space
			for (User u : tm.getUsers()){
				if(u.getPlayerID()==existingUser.getPlayerID()){ //if the player is already in the game, updates that player
					tm.getUser(userID).setColor(nuColor);
					return new JsonPrimitive("Success");
				}
			}
			try {
				User copyUser = existingUser.clone();
				copyUser.setColor(existingUser.getCatanColor());
				tm.addUser(copyUser);
			} catch (ModelException e) {
				throw new ServerInvalidRequestException("Cannot join. Game already full.");
			}
			return new JsonPrimitive("Success");
			
		}
		else if(tm.getUsers().size() == 4){ //if the game is full and our user is rejoining
			for (User u : tm.getUsers()){
				if(u.getPlayerID()==existingUser.getPlayerID()){
					tm.getUser(userID).setColor(nuColor);
					return new JsonPrimitive("Success");
				}
			}
		}
		throw new ServerInvalidRequestException("Cannot join. Game already full.");

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
		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		Model model = modelFacade.getModel();
		
		return model.serialize();
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
	public JsonElement resetGame(int gameId) throws ServerInvalidRequestException 
	{
		//if we save the initial state of the game when it is first created
		//all this has to do is call load on that saved file
		
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
		
		this.updateModelVersion(gameId);
		
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
		
		//access objects of the specific game
		Game game = gameManager.getGameById(gameId);
		if (game == null)
		{
			throw new ServerInvalidRequestException("Incorrect game id: " + gameId);
		}
		if (playerIndex < 0 || playerIndex > 3)
		{
			throw new ServerInvalidRequestException("Invalid player index.");
		}
		if (rolledNumber < 2 || rolledNumber > 12)
		{
			throw new ServerInvalidRequestException("Invalid number rolled.");
		}
		
		ModelFacade modelFacade = game.getModelFacade();
		Model model = modelFacade.getModel();
		TurnManager turnManager = modelFacade.turnManager();
		User user = turnManager.getUserFromIndex(playerIndex);
		
		if (modelFacade.canRollNumber(turnManager, user))
		{
			if (rolledNumber != 7)
			{
				modelFacade.givePlayersResourcesFromRoll(rolledNumber);
			}
			else //rolledNumber == 7
			{
				if (modelFacade.discardPhaseNeeded())
				{
					modelFacade.updateTurnPhase(TurnPhase.DISCARDING);
				}
				else
				{
					modelFacade.updateTurnPhase(TurnPhase.ROBBING);
				}
			}
			
			//add to the game log
			String logSource = user.getName();
			String logMessage = user.getName() + "rolled a " + rolledNumber + ".";
			MessageLine logEntry = new MessageLine(logMessage, logSource);
			modelFacade.addToGameLog(logEntry);
			
			this.updateModelVersion(gameId);
		}
		else
		{
			throw new ServerInvalidRequestException();
		}
		
		return getModel(0, gameId);
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
	public JsonElement robPlayer(int gameId, int playerIndex, int victimIndex, 
			HexLocation location, boolean soldierCard) throws ServerInvalidRequestException 
	{		
		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		Model model = modelFacade.getModel();
		TurnManager turnManager = modelFacade.turnManager();
		User user = turnManager.getUserFromIndex(playerIndex);
		
		if (game == null)
		{
			throw new ServerInvalidRequestException("Invalid game ID");
		}
		if (playerIndex < 0 || playerIndex > 3)
		{
			throw new ServerInvalidRequestException("Invalid player index");
		}
		if (victimIndex < -1 || victimIndex > 3)
		{
			throw new ServerInvalidRequestException("Invalid player index");
		}
		if (location == null)
		{
			throw new ServerInvalidRequestException("Missing hex location");
			//TODO need a check if the location is valid?
		}

		if (victimIndex == -1)
		{
			modelFacade.map().updateRobberLocation(location);
			
			if (soldierCard)
			{
				 playSoldierCard(game, user, modelFacade);
			}
			
			String logSource = user.getName();
			String logMessage = user.getName() + "moved the robber and robbed, but couldn't rob anyone";
			MessageLine logEntry = new MessageLine(logMessage, logSource);
			modelFacade.addToGameLog(logEntry);

		}
		else
		{
			User victim = turnManager.getUserFromIndex(victimIndex);

			if (modelFacade.canRobPlayer(modelFacade.getHexTileFromHexLoc(location), user, victim))
			{
				modelFacade.map().updateRobberLocation(location);

				//randomly select a resource card from the victim
				ResourceCard stolenCard = victim.getResourceCards().getRandomResourceCard();

				//remove it from the victim and add it to the given player
				victim.getResourceCards().removeResourceCard(stolenCard);
				user.getResourceCards().addResourceCard(stolenCard);

				if (soldierCard)
				{
					 playSoldierCard(game, user, modelFacade);
				}

				//update game log with appropriate robbing message
				String logSource = user.getName();
				String logMessage = user.getName() + "moved the robber and robbed " + victim.getName();
				MessageLine logEntry = new MessageLine(logMessage, logSource);
				modelFacade.addToGameLog(logEntry);
			}
			else
			{
				throw new ServerInvalidRequestException();
			}
		}
		modelFacade.updateTurnPhase(TurnPhase.PLAYING);
		
		return getModel(0, gameId);
	}
	
	public void playSoldierCard(Game game, User user, ModelFacade modelFacade)
	{
		//remove a soldier devcard from the player
		user.getUsableDevCardDeck().removeDevCard(new DevCard(DevCardType.SOLDIER));
		//add one to the number of soldiers the player has played
		user.setSoldiers(user.getSoldiers() + 1);

		//check to see if they gained the largest army
		updateLargestArmy(game, modelFacade);

		//update game log with appropriate soldier message(s)
		String logSource = user.getName();
		String logMessage = user.getName() + " played a soldier card";
		MessageLine logEntry = new MessageLine(logMessage, logSource);
		modelFacade.addToGameLog(logEntry);
	}
	
	public void updateLargestArmy(Game game, ModelFacade modelFacade)
	{
		game.calcLargestArmyPlayer();
		int largestArmyIndex = game.getLargestArmyPlayer();
		modelFacade.score().setLargestArmyUser(largestArmyIndex);
		
		updatePlayerScores(game);
	}
	
	public void updatePlayerScores(Game game)
	{
		ModelFacade modelFacade = game.getModelFacade();
		TurnManager turnManager = modelFacade.turnManager();
		List<User> users = turnManager.getUsers();
		for (User user : users)
		{
			int score = 0;
			score += user.getNumSettlementsOnMap();
			int cities = user.getNumCitiesOnMap();
			score += (cities * 2);
			score += user.getMonumentsPlayed();
			if (modelFacade.score().getLargestArmyUser() == user.getTurnIndex())
			{
				score += 2;
			}
			if (modelFacade.score().getLongestRoadUser() == user.getTurnIndex())
			{
				score += 2;
			}
			
			if (score >= 10)
			{
				modelFacade.score().setWinner(user.getTurnIndex());
			}
		}
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
		ModelFacade facade = gameManager.getGameById(gameId).getModelFacade();
		TurnManager tm = facade.turnManager();
		User user = tm.getUserFromIndex(playerIndex);
		if (facade.canFinishTurn(tm, tm.getUserFromIndex(playerIndex))){
			TurnPhase currentPhase = tm.currentTurnPhase();
			
			switch (currentPhase){
			case PLAYING:
				//if curr player has any dev cards they bought, add them to usable
				user.getHand().moveNewToUsable();
				
				//update the user booleans -- hasDiscarded, hasPlayedDevCard
				user.setHasDiscarded(false);
				user.setHasPlayedDevCard(false);
				
				tm.setCurrentPhase(TurnPhase.ROLLING);
				tm.setCurrentTurn(nextTurn(playerIndex));
				break;
			case FIRSTROUND:
				if (tm.getCurrentTurn() != 3)
					tm.setCurrentTurn(nextTurn(playerIndex));
				else{
					tm.setCurrentPhase(TurnPhase.SECONDROUND);
				}
				break;
			case SECONDROUND:
				if (tm.getCurrentTurn() != 0)
					tm.setCurrentTurn(playerIndex - 1);
				else{
					tm.setCurrentPhase(TurnPhase.ROLLING);
				}
				break;
			default: //can't end turn in any other phase
				throw new ServerInvalidRequestException("Cannot finish turn at this time");
			}
			updateModelVersion(gameId);
			
			//add to the game log
			String logSource = user.getName();
			String logMessage = user.getName() + "finished their turn";
			MessageLine logEntry = new MessageLine(logMessage, logSource);
			facade.addToGameLog(logEntry);
		}
		else{
			throw new ServerInvalidRequestException();
		}
		return null;
	}
	
	public int nextTurn(int playerIndex){
		int nextIndex = 0;
		if (playerIndex == 3)
			nextIndex = 0;
		else{
			nextIndex = playerIndex +1;
		}
		return nextIndex;
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
		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		TurnManager turnManager = modelFacade.turnManager();
		User user = turnManager.getUserFromIndex(playerIndex);
		
		if(modelFacade.canBuyDevCard(turnManager, user, modelFacade.bank().getDevCardDeck())) {
			
			//remove resources from player
			user.getResourceCards().removeResourceCard(new ResourceCard(ResourceType.ORE));
			user.getResourceCards().removeResourceCard(new ResourceCard(ResourceType.SHEEP));
			user.getResourceCards().removeResourceCard(new ResourceCard(ResourceType.WHEAT));
			
			//add resources to bank
			modelFacade.bank().getResourceDeck().addResourceCard(new ResourceCard(ResourceType.ORE));
			modelFacade.bank().getResourceDeck().addResourceCard(new ResourceCard(ResourceType.SHEEP));
			modelFacade.bank().getResourceDeck().addResourceCard(new ResourceCard(ResourceType.WHEAT));
			
			//randomly give the user a dev card 
			DevCard devCard = modelFacade.bank().getDevCardDeck().getRandomCard();
			modelFacade.bank().getDevCardDeck().removeDevCard(devCard);
			user.getNewDevCardDeck().addDevCard(devCard);
			
			//update game history
			String logSource = user.getName();
			String logMessage = user.getName() + " bought a development card.";
			MessageLine logEntry = new MessageLine(logMessage, logSource);
			modelFacade.addToGameLog(logEntry);
			
			updateModelVersion(gameId);
		}
		else{
			throw new ServerInvalidRequestException("Cannot buy a development card at this time");
		}
		
		return getModel(0, gameId);
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
		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		TurnManager turnManager = modelFacade.turnManager();
		User user = turnManager.getUserFromIndex(playerIndex);
		
		DevCard devCard = new DevCard(DevCardType.YEAR_OF_PLENTY);
		
		if(modelFacade.canPlayDevCard(turnManager, user, devCard) && 
				modelFacade.canPlayYearofPlenty(turnManager, user, modelFacade.bank(), new ResourceCard(resource1), new ResourceCard(resource2))) {
			//add 1 of each of the specified resources to the player's resources
			user.getResourceCards().addResourceCard(new ResourceCard(resource1));
			user.getResourceCards().addResourceCard(new ResourceCard(resource2));
			//subtract the same resources from the resource bank
			modelFacade.bank().getResourceDeck().removeResourceCard(new ResourceCard(resource1));
			modelFacade.bank().getResourceDeck().removeResourceCard(new ResourceCard(resource2));
			//subtract one from the player's year of plenty cards
			user.getUsableDevCardDeck().removeDevCard(devCard);
			//update game history
			String logSource = user.getName();
			String logMessage = user.getName() + " played year of plenty.";
			MessageLine logEntry = new MessageLine(logMessage, logSource);
			modelFacade.addToGameLog(logEntry);
			
			updateModelVersion(gameId);
		}
		else{
			throw new ServerInvalidRequestException("Cannot play this card right now");
		}
		
		return getModel(0, gameId);
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
		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		TurnManager turnManager = modelFacade.turnManager();
		User user = turnManager.getUserFromIndex(playerIndex);
		
		DevCard devCard = new DevCard(DevCardType.ROAD_BUILD);
		
		if(modelFacade.canPlayDevCard(turnManager, user, devCard) && modelFacade.canPlayRoadBuilding(turnManager, user)) {
			//subtract 1 from player's road building
			user.getUsableDevCardDeck().removeDevCard(devCard);
			//let player build two roads on given edges
			buildRoadHelper(modelFacade, playerIndex, location1, true);
			buildRoadHelper(modelFacade, playerIndex, location2, true);
			//re calculate longest road
			game.calcLongestRoadPlayer();
			int longestRoadPlayer = game.getLongestRoadPlayer(); 
			modelFacade.score().setLongestRoadUser(longestRoadPlayer);
			//update game history
			String logSource = user.getName();
			String logMessage = user.getName() + " played road building and built two roads.";
			MessageLine logEntry = new MessageLine(logMessage, logSource);
			modelFacade.addToGameLog(logEntry);
			
			updateModelVersion(gameId);
		}
		else{
			throw new ServerInvalidRequestException("Cannot play this card right now");
		}
		
		return getModel(0, gameId);
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
		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		TurnManager turnManager = modelFacade.turnManager();
		User user = turnManager.getUserFromIndex(playerIndex);
		
		DevCard devCard = new DevCard(DevCardType.MONOPOLY);
		
		if(modelFacade.canPlayDevCard(turnManager, user, devCard) && modelFacade.canPlayMonopoly(turnManager, user, resource)) {
			//get total number of resources from users besides currUser
			int totalResources = getResourceCount(turnManager.getUsers(), playerIndex, resource);
			//remove resources from other players
			removeAllResource(turnManager.getUsers(), playerIndex, resource);
			//give the user all those cards
			for(int i = 0; i < totalResources; i++) {
				user.getResourceCards().addResourceCard(new ResourceCard(resource));
			}
			//remove user's monopoly card
			user.getUsableDevCardDeck().removeDevCard(devCard);
			//update game history
			String logSource = user.getName();
			String logMessage = user.getName() + " played monopoly.";
			MessageLine logEntry = new MessageLine(logMessage, logSource);
			modelFacade.addToGameLog(logEntry);
			
			updateModelVersion(gameId);
		}
		else{
			throw new ServerInvalidRequestException("Cannot play this card right now");
		}
		
		return getModel(0, gameId);
	}
	
	/**
	 * counts the total number of cards of a given resource all users have. 
	 * used for monopoly
	 * @param userIndex
	 * @return
	 */
	private int getResourceCount(List<User> users, int userIndex, ResourceType resource) {
		int total = 0;
		for(User user : users) {
			if(user.getTurnIndex() == userIndex) {
				continue;
			}
			total += user.getResourceCards().getCountByType(resource);
		}
		return total;
	}
	
	/**
	 * removes all of one type of resource from user's resource deck
	 * used for monopoly
	 * @param users
	 * @param userIndex
	 * @param resource
	 */
	private void removeAllResource(List<User> users, int userIndex, ResourceType resource) {
		for(User user : users) {
			if(user.getTurnIndex() == userIndex) {
				continue;
			}
			for(int i = 0; i < user.getResourceCards().getCountByType(resource); i++) {
				user.getResourceCards().removeResourceCard(new ResourceCard(resource));
			}
		}
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
		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		TurnManager turnManager = modelFacade.turnManager();
		User user = turnManager.getUserFromIndex(playerIndex);
		
		DevCard devCard = new DevCard(DevCardType.MONUMENT);
		
		if(modelFacade.canPlayDevCard(turnManager, user, devCard) && modelFacade.canPlayMonument(turnManager, user)) {
			//if user can play monument
			//get rid of dev card from usable deck 
			user.getUsableDevCardDeck().removeDevCard(devCard);
			//update user points
			user.setMonumentsPlayed(user.getMonumentsPlayed() + 1);
			user.setVictoryPoints(user.getVictoryPoints() + 1);
			//update game history
			String logSource = user.getName();
			String logMessage = user.getName() + " played a monument and gained a point.";
			MessageLine logEntry = new MessageLine(logMessage, logSource);
			modelFacade.addToGameLog(logEntry);
			
			updateModelVersion(gameId);
		}
		else{
			throw new ServerInvalidRequestException("Cannot play this card right now");
		}
		
		return getModel(0, gameId);
	}
	
	/**
	 * helper function that can be used by build road and road-build dev card
	 * checks if user can place road at given edge, and update model
	 * @param game
	 * @param playerIndex
	 * @param roadLocation
	 * @throws ServerInvalidRequestException 
	 */
	private void buildRoadHelper(ModelFacade modelFacade, int playerIndex, EdgeLocation roadLocation, boolean free) throws ServerInvalidRequestException {
		TurnManager turnManager = modelFacade.turnManager();
		User user = turnManager.getUserFromIndex(playerIndex);
		if(modelFacade.canPlaceRoadAtLoc(turnManager, roadLocation, user)
				&& modelFacade.canBuyRoadForLoc(turnManager, roadLocation, user, free)) {
			//Reduces by a road
			user.setUnusedRoads(user.getUnusedRoads()-1);
			//create a new road
			Road road = new Road();
			road.setEdge(new Edge(roadLocation));
			//add road to map
			modelFacade.map().addRoad(road);
		}
		else{
			throw new ServerInvalidRequestException("Cannot buy or build road at this location"); 
		}
		
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
		Game game = gameManager.getGameById(gameId);
		ModelFacade facade = game.getModelFacade();
		TurnManager tm = facade.turnManager();
		User curUser = tm.getUserFromIndex(playerIndex);
	
		buildRoadHelper(facade, playerIndex, roadLocation, free);
		
		//Pay Resources
		if (!free){
			curUser.getResourceCards().removeResourceCard(new ResourceCard(ResourceType.BRICK));
			curUser.getResourceCards().removeResourceCard(new ResourceCard(ResourceType.WOOD));
			//Give to the bank
			facade.bank().getResourceDeck().addResourceCard(new ResourceCard(ResourceType.BRICK));
			facade.bank().getResourceDeck().addResourceCard(new ResourceCard(ResourceType.WOOD));
		}
		
		//re calculate longest road
		game.calcLongestRoadPlayer();
		int longestRoadPlayer = game.getLongestRoadPlayer(); 
		facade.score().setLongestRoadUser(longestRoadPlayer);
		
		//Update history
		String user = curUser.getName();
		String message = user + "built a road";
		MessageLine line = new MessageLine(message, user);
		facade.getModel().getLog().addLine(line);
		updateModelVersion(gameId);
		
		
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
	public JsonElement buildSettlement(int gameId, int playerIndex, 
			VertexLocation vertexLocation, boolean free) throws ServerInvalidRequestException 
	{
		ModelFacade facade = gameManager.getGameById(gameId).getModelFacade();
		TurnManager tm = facade.turnManager();
		User curUser = tm.getUserFromIndex(playerIndex);
		
		if (facade.canPlaceBuildingAtLoc(tm, vertexLocation, curUser, PieceType.SETTLEMENT)
				&& facade.canBuyPiece(tm, curUser, PieceType.SETTLEMENT)){
			
			//Decrease available Settlements
			curUser.setUnusedSettlements(curUser.getUnusedSettlements()-1);
			
			Building settlement = new Building();
			settlement.setVertex(new Vertex(vertexLocation));
			
			//Add City to map
			facade.getModel().getMap().addSettlement(settlement);
			
			//Pay the resources
			if (!free){
				ArrayList<ResourceCard> paymentCards = new ArrayList<ResourceCard>();
				paymentCards.add(new ResourceCard(ResourceType.BRICK));
				paymentCards.add(new ResourceCard(ResourceType.SHEEP));
				paymentCards.add(new ResourceCard(ResourceType.WHEAT));
				paymentCards.add(new ResourceCard(ResourceType.WOOD));
				
				ResourceCardDeck payment = new ResourceCardDeck(paymentCards);
				
				//user pays the resources
				removeResources(payment, curUser.getResourceCards());
			
				//Give to the bank
				addResources(payment, facade.bank().getResourceDeck());
			}
			
			//Add points
			curUser.setVictoryPoints(curUser.getVictoryPoints()+1);
			
			//Update history
			String user = curUser.getName();
			String message = user + "built a settlement";
			MessageLine line = new MessageLine(message, user);
			facade.getModel().getLog().addLine(line);
			
			updateModelVersion(gameId);
		}
		else{	
			throw new ServerInvalidRequestException();
		}
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
	public JsonElement buildCity(int gameId, int playerIndex, 
			VertexLocation vertexLocation) throws ServerInvalidRequestException 
	{
		ModelFacade facade = gameManager.getGameById(gameId).getModelFacade();
		TurnManager tm = facade.turnManager();
		User curUser = tm.getUserFromIndex(playerIndex);
		
		if (facade.canPlaceBuildingAtLoc(tm, vertexLocation, curUser, PieceType.CITY)
				&& facade.canBuyPiece(tm, curUser, PieceType.CITY)){
			
			//Decrease available Cities
			curUser.setUnusedCities(curUser.getUnusedCities()-1);
			//Increase available Settlements
			curUser.setUnusedSettlements(curUser.getUnusedSettlements()+1);
			
			Building city = new Building();
			city.setVertex(new Vertex(vertexLocation));
			//Remove Settlement from vertex
			facade.getModel().getMap().removeSettlement(city);
			//Add City to map
			facade.getModel().getMap().addCity(city);
			
			ArrayList<ResourceCard> paymentCards = new ArrayList<ResourceCard>();
			for(int i = 0; i < 3; i++)
				paymentCards.add(new ResourceCard(ResourceType.ORE));
			for(int i = 0; i < 2; i++)
				paymentCards.add(new ResourceCard(ResourceType.WHEAT));
			ResourceCardDeck payment = new ResourceCardDeck(paymentCards);
			//Pay the resources
			
			removeResources(payment, curUser.getResourceCards());
			
			//Give to the bank
			addResources(payment, facade.bank().getResourceDeck());
			
			//Add points
			curUser.setVictoryPoints(curUser.getVictoryPoints()+1);
			
			//Update history
			String user = curUser.getName();
			String message = user + "built a city";
			MessageLine line = new MessageLine(message, user);
			facade.getModel().getLog().addLine(line);
			
			updateModelVersion(gameId);
		}
		else{	
			throw new ServerInvalidRequestException();
		}
			
		
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
		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		TurnManager turnManager = modelFacade.turnManager();
		User user = turnManager.getUserFromIndex(playerIndex);
		User receivingUser = turnManager.getUserFromIndex(receiver);
		
		TradeOffer tradeOffer = new TradeOffer(receiverDeck, senderDeck);
		
		if(modelFacade.canOfferTrade(turnManager, user, receivingUser, tradeOffer)) {
			modelFacade.getModel().setTradeOffer(tradeOffer);
			String logSource = user.getName();
			String logMessage = user.getName() + " offered a trade to" + receivingUser.getName() + ".";
			MessageLine logEntry = new MessageLine(logMessage, logSource);
			modelFacade.addToGameLog(logEntry);
			
			updateModelVersion(gameId);
		}
		else{
			throw new ServerInvalidRequestException();
		}
		return getModel(0, gameId);
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
		
		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		TurnManager turnManager = modelFacade.turnManager();
		User user = turnManager.getUserFromIndex(playerIndex);
		
		TradeOffer tradeOffer = modelFacade.getModel().getTradeOffer();
		
		if(modelFacade.canAcceptTrade(turnManager, user, tradeOffer)) {
			if(accept) {
				//trade offer goes through, swap resources
				
				User trader = turnManager.getUserFromIndex(tradeOffer.getSenderIndex());
				ResourceCardDeck sendDeck = tradeOffer.getSendingDeck(); //deck trader offered
				ResourceCardDeck receiveDeck = tradeOffer.getReceivingDeck(); // the deck the trader receives
				
				addResources(trader.getResourceCards(), receiveDeck);
				removeResources(user.getResourceCards(), receiveDeck);
				
				addResources(user.getResourceCards(), sendDeck);
				removeResources(trader.getResourceCards(), sendDeck);
				
				//update game log
				String logSource = user.getName();
				String logMessage = user.getName() + " accepted the trade.";
				MessageLine logEntry = new MessageLine(logMessage, logSource);
				modelFacade.addToGameLog(logEntry);
			}
			else{
				//trade not accepted
				//update game log
				String logSource = user.getName();
				String logMessage = user.getName() + " rejected the trade.";
				MessageLine logEntry = new MessageLine(logMessage, logSource);
				modelFacade.addToGameLog(logEntry);
				
			}
			//remove trade offer from model
			modelFacade.getModel().setTradeOffer(null);
			
			updateModelVersion(gameId);
		}
		else{
			throw new ServerInvalidRequestException();
		}
		
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

		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		TurnManager turnManager = modelFacade.turnManager();
		User user = turnManager.getUserFromIndex(playerIndex);
		
		ResourceCard wantedCard = new ResourceCard(receivingResource);
		ArrayList<ResourceCard> offeredCards = new ArrayList<ResourceCard>();
		for(int i = 0; i < ratio; i++) {
			offeredCards.add(new ResourceCard(sendingResource));
		}
		ResourceCardDeck offeredCardsDeck = new ResourceCardDeck(offeredCards);
		
		//if can maritime trade
		if(modelFacade.canMaritimeTrade(turnManager, modelFacade.bank(), user, wantedCard, offeredCardsDeck)) {
			modelFacade.bank().getResourceDeck().removeResourceCard(wantedCard);
			addResources(modelFacade.bank().getResourceDeck(), offeredCardsDeck);
			removeResources(user.getResourceCards(), offeredCardsDeck);
			user.getResourceCards().addResourceCard(wantedCard);
			
			String logSource = user.getName();
			String logMessage = user.getName() + " did a maritime trade.";
			MessageLine logEntry = new MessageLine(logMessage, logSource);
			modelFacade.addToGameLog(logEntry);
			
			updateModelVersion(gameId);
		}
		else{
			throw new ServerInvalidRequestException();
		}
		return getModel(0, gameId);
	}
	
	//helper function to remove cards from a resource deck
	private void removeResources(ResourceCardDeck cardsToRemove, ResourceCardDeck deckToRemoveFrom) {
		for(ResourceCard card : cardsToRemove.getAllResourceCards()) {
			deckToRemoveFrom.removeResourceCard(card);
		}
	}
	
	//helper function to add cards to a resource deck
	private void addResources(ResourceCardDeck cardsToAdd, ResourceCardDeck deckToAddTo) {
		for(ResourceCard card : cardsToAdd.getAllResourceCards()) {
			deckToAddTo.addResourceCard(card);
		}
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
		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		TurnManager turnManager = modelFacade.turnManager();
		User user = turnManager.getUserFromIndex(playerIndex);
		ArrayList<ResourceCard> resources = (ArrayList<ResourceCard>) resourcesToDiscard.getAllResourceCards();
		
		if(modelFacade.canDiscardCards(turnManager, user, resources)) {
			//subtract the resources in the given resource card deck from the player
			for(ResourceCard card : resources) {
				user.getResourceCards().removeResourceCard(card);
			}
			//add the same resources to the resource bank
			for(ResourceCard card : resources) {
				modelFacade.bank().getResourceDeck().addResourceCard(card);
			}
			//set the user's discard bool to true
			user.setHasDiscarded(true);
			//if all players who needed to discard have discarded
			if(checkAllDiscarded(turnManager.getUsers())) {
				modelFacade.updateTurnPhase(TurnPhase.ROBBING);
			}
			updateModelVersion(gameId);
		}
		else{
			throw new ServerInvalidRequestException();
		}
		
		//need to return a new model?
		return getModel(0, gameId);
	}
	
	//helper function to check that all users have discarded
	private boolean checkAllDiscarded(List<User> users) {
		for(User user : users) {
			//if user hasn't discarded but has more than 7 cards, return false
			if(!user.getHasDiscarded() && user.getResourceCards().getAllResourceCards().size() > 7) {
				return false;
			}
		}
		return true;
	}
	
	public void updateModelVersion(int gameId)
	{
		Game game = gameManager.getGameById(gameId);
		ModelFacade modelFacade = game.getModelFacade();
		Model model = modelFacade.getModel();
		model.incrementVersion();
	}
	
	
	/////////////Getters and Setters/////////////////
	
	public GameManager getGameManager() {
		return gameManager;
	}
	
	public UserManager getUserManager() {
		return userManager;
	}
}
