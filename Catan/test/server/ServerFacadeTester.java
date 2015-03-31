package server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.command.game.*;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import server.game.Game;
import server.game.GameManager;
import shared.definitions.ResourceType;
import shared.model.cards.ResourceCard;
import shared.model.cards.ResourceCardDeck;
import shared.model.game.User;

public class ServerFacadeTester {
	private ServerFacade facade;
	
	public ServerFacadeTester(){
		facade = ServerFacade.instance();
	}
	
	@Before
	public void setUp(){
		
	}
	
	public JsonElement extractJson(String fileName) throws ServerInvalidRequestException {
		if (fileName == null)
		{
			throw new ServerInvalidRequestException("Missing fileName field");
		}
		
		String jsonStr = "";
		JsonObject jsonModel = null;
		String path = "test/jsons/";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path + fileName + ".txt"));
			
			String currLine = "";
			
			while((currLine = reader.readLine()) != null) {
				jsonStr += currLine;
			}
			
			reader.close();
			
			jsonModel = new JsonParser().parse(jsonStr).getAsJsonObject();
			
		} catch (IOException e) {

			e.printStackTrace();
			throw new ServerInvalidRequestException("Unable to load file");
		}
		
		return jsonModel;
	}
	
	public void setGame(String fileName) throws ServerInvalidRequestException {
		
		JsonObject jsonModel = extractJson(fileName).getAsJsonObject();
		
		GameManager gameManager = facade.getGameManager();
		Game nuGame = gameManager.getGameById(0);
		nuGame.getModelFacade().updateModel(jsonModel);
	}
	

	@Test
	public void createGame(){
		//valid request
		try {
			facade.createNewGame("testingGame", true, true, true);
		} catch (ServerInvalidRequestException e) {
			fail();
		}
		//null game title
		try {
			facade.createNewGame(null, true, true, true);
			fail();
		} catch (ServerInvalidRequestException e) {
			assert(true);
		}
		//testing with empty boolean value
		try {
			facade.createNewGame("happy string", null, true, true);
			fail();
		} catch (ServerInvalidRequestException e) {
			assert(true);
		}
	}
	
	@Test
	public void register(){
		//Valid
		try{
			facade.register("Kent", "12345");
		}
		catch (ServerInvalidRequestException e) {
			fail();
		}
		
		//Invalid - username too short
		try{
			facade.register("Uh", "12345");
			fail();
		}
		catch (ServerInvalidRequestException e) {
		}
		
		//Invalid - password too short
		try{
			facade.register("Greg", "123");
			fail();
		}
		catch (ServerInvalidRequestException e) {
		}
	}
	
	@Test
	public void login(){
		try{
			facade.login("Sam", "sam");
		}
		catch (ServerInvalidRequestException e) {
			fail();
		}
	}
	
	
	@Test
	public void joinGame(){
		//log in, valid request
		try {
			facade.joinGame(0, "blue", 1);
		} catch (ServerInvalidRequestException e) {
			fail();
		}
		
		//nonexistent game
		try {
			facade.joinGame(5, "blue", 1);
			fail();
		} catch (ServerInvalidRequestException e) {
			
		}
		//color already chosen
		try {
			facade.joinGame(0, "blue", 2);
			fail();
		} catch (ServerInvalidRequestException e) {
			
		}
	}
	
	@Test
	public void list(){
		try {
			facade.gameList();
		}
		catch (ServerInvalidRequestException e){
			fail();
		}
	}
			
	@Test
	public void getModel(){
		//Correct ID
		try{
			facade.getModel(0, 0);
		}
		catch (ServerInvalidRequestException e){
			fail();
		}
		// Bad ID
		try{
			facade.getModel(0, 68);
			fail();
		}
		catch (ServerInvalidRequestException e){
		}
	}
	
	@Test
	public void rollNumber() {
		//incorrect turn phase
		try {
			setGame("discardPhase");
			facade.rollNumber(0, 0, 6);
			fail("rollNumber: Exception not thrown, incorrect phase");
			
		} catch (ServerInvalidRequestException e) {
			// ok
			e.printStackTrace();
		}
		//not user's turn
		try {
			setGame("testPlayer2Turn");
			facade.rollNumber(0, 0, 6);
			fail("rollNumber: Exception not thrown, incorrect player");
		} catch (ServerInvalidRequestException e) {
			// ok
			e.printStackTrace();
		}
		
		//ok test case, turnphase updated, players with buildings on number hex gain resources
		try {
			setGame("samCanRoll");
			facade.rollNumber(0, 0, 6);
			//assert equals that new model is as expected
			String expectedJsonStr = extractJson("samCanRollResult").getAsString();
			String actualJsonStr = facade.getGameManager().getGameById(0).getModelFacade().getModel().serialize().getAsString();
			assertTrue(expectedJsonStr.equals(actualJsonStr));
		} catch (ServerInvalidRequestException e) {
			fail("roll number: should have passed");
			e.printStackTrace();

		}
	}
	
	@Test
	public void SaveNLoad(){
		//Valid
		try{
			facade.gameSave(0, "saveTest");
		}
		catch (ServerInvalidRequestException e){
			fail();
		}
		//Valid
		try{
			facade.gameLoad("saveTest");
		}
		catch (ServerInvalidRequestException e){
			fail();
		}
		//Invalid
		try{
			facade.gameSave(68, "saveTestfail");
			fail();
		}
		catch (ServerInvalidRequestException e){
		}
		//Invalid
		try{
			facade.gameLoad("savvvveTest");
			fail();
		}
		catch (ServerInvalidRequestException e){
		}
		
	}
	
	@Test
	public void reset(){
		
	}
	@Test
	public void robPlayer() {
		//incorrect turn phase
		//not user's turn
		//robber is in same place
		//player being robbed has no resource cards
		//ok test case - robber is moved, player robbed has given up resource
	}
	
	@Test
	public void finishTurn() {
		//incorrect turn phase
		//not user's turn
		//ok test case, player index is updated
	}
	
	@Test
	public void buyDevCard() {
		//incorrect turn phase
		//not user's turn
		//user doesn't have resources
		//bank out of dev cards
		//ok test case, user gains a dev card in newDevCards and loses resources
	}
	
	@Test
	public void yearOfPlenty() {
		//incorrect turn phase
		//not user's turn
		//user doesn't have that card
		//bank doesn't have wanted resources
		//ok test case, user uses card and gets two cards from bank
	}
	
	@Test
	public void roadBuild() {
		//incorrect turn phase
		//not user's turn
		//user doesn't have that card
		//ok test case, user uses the card and builds two roads
	}
	
	@Test
	public void soldier() {
		//incorrect turn phase
		//not user's turn
		//user doesn't have that card
		//ok test case, user moves the robber and robs someone, if available
	}
	
	@Test
	public void monopoly() {
		//incorrect turn phase
		//not user's turn
		//user doesn't have that card
		//ok test case, user gains all resources from other players of resource type
	}
	
	@Test
	public void monument() {
		//incorrect turn phase
		//not user's turn
		//user doesn't have that card
		//ok test case, user gains a point and uses the card
	}
	
	@Test
	public void buildRoad() {
		//incorrect turn phase
		//not user's turn
		//not enough resources for road
		//not linked to another settlement or road owned by same user
		//ok test case, user now has road
	}

	
	@Test
	public void buildSettlement() {
		//incorrect turn phase
		//not user's turn
		//not enough resources for settlement
		//not linked to roads 
		//invalid build location (next to some other player's settlement)
		//ok test case, user now has settlement
	}
	
	@Test
	public void buildCity() {
		//incorrect turn phase
		//not user's turn
		//doesn't have enough resources for a city
		//doesn't have a settlement in the place of a city
		//ok test case, user now has a city
	}
	
	@Test
	public void offerTrade() {
		//incorrect turn phase
		//not user's turn
		//don't have resources offering
		//ok test case, model now has trade offer
		
	}
	
	@Test
	public void acceptTrade() {
		//user not offered a trade
		//if there is no trade offer
		//user doesn't have required resources to accept
		//user accepts the trade, resources for both players should be updated, trade offer removed
	}
	
	@Test
	public void maritimeTrade() {
		//wrong turn phase
		//not user's turn
		//no necessary resources
		//bank doesn't have card wanted
		//regular maritime trade, trade goes through, resources updated
		//user gives 3, but doesn't have 3 port
		//user gives 2, but doesn't have 2 port
		//user has 3 port, trade goes through, resources updated
		//user has 2 port, trade goes through, resources updated
	}
	
	@Test
	public void discardCards() {
		//incorrect turn phase
		try {
			setGame("samCanRoll");
			facade.discardCards(0, 0, new ResourceCardDeck(new ArrayList<ResourceCard>()));
			fail("discard: should've thrown exception, incorrect phase");
		} catch (ServerInvalidRequestException e) {
			//ok
			e.printStackTrace();
		}
		//user doesn't have more than 7 cards
		try {
			setGame("discardPhase");
			User user = facade.getGameManager().getGameById(0).getModelFacade().turnManager().getUserFromIndex(1);
			facade.discardCards(0, 1, user.getResourceCards());
			fail("discard: should've thrown exception, user doesn't have enough resources");
		} catch (ServerInvalidRequestException e) {
			// ok
			e.printStackTrace();
		}
		//user has more than 7 cards
		
		//trying to discard cards they don't have
		try {
			ArrayList<ResourceCard> resources = new ArrayList<ResourceCard>();
			resources.add(new ResourceCard(ResourceType.BRICK));
			resources.add(new ResourceCard(ResourceType.BRICK));
			resources.add(new ResourceCard(ResourceType.WHEAT));
			resources.add(new ResourceCard(ResourceType.ORE));
			ResourceCardDeck resourcesToDiscard = new ResourceCardDeck(resources);
			facade.discardCards(0, 0, resourcesToDiscard);
			fail("discard: should've thrown exception, discarding cards user doesn't have");
		} catch (ServerInvalidRequestException e) {
			// ok
			e.printStackTrace();
		}
		//ok test case, user loses their discarded cards
		try {
			ArrayList<ResourceCard> resources = new ArrayList<ResourceCard>();
			resources.add(new ResourceCard(ResourceType.BRICK));
			resources.add(new ResourceCard(ResourceType.ORE));
			resources.add(new ResourceCard(ResourceType.ORE));
			resources.add(new ResourceCard(ResourceType.ORE));
			ResourceCardDeck resourcesToDiscard = new ResourceCardDeck(resources);
			facade.discardCards(0, 0, resourcesToDiscard);
			String expectedJsonStr = extractJson("samDiscardedResult").getAsString();
			String actualJsonStr = facade.getGameManager().getGameById(0).getModelFacade().getModel().serialize().getAsString();
			assertTrue(expectedJsonStr.equals(actualJsonStr));
		} catch (ServerInvalidRequestException e) {
			fail("discard: shouldn't have failed");
			e.printStackTrace();
		}
	}
}
