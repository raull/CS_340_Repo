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
import shared.model.Model;
import shared.model.cards.ResourceCard;
import shared.model.cards.ResourceCardDeck;
import shared.model.game.TurnPhase;
import shared.model.game.User;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.model.board.HexTile;
import shared.model.facade.ModelFacade;

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
			//model should now be in discarding, robbing, or playing
			Model currModel = facade.getGameManager().getGameById(0).getModelFacade().getModel();
			assertTrue(currModel.getTurnManager().currentTurnPhase() == TurnPhase.PLAYING || 
					currModel.getTurnManager().currentTurnPhase() == TurnPhase.ROBBING ||
					currModel.getTurnManager().currentTurnPhase() == TurnPhase.DISCARDING);
			//players got their resources
			//sam got an ore
			assertTrue(currModel.getTurnManager().getUserFromIndex(0).getResourceCards().getCountByType(ResourceType.ORE) == 1);
			//mark got a wood
			assertTrue(currModel.getTurnManager().getUserFromIndex(1).getResourceCards().getCountByType(ResourceType.WOOD) == 2);
			
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
		try{
			
			//Places a road
			int newID = facade.getGameManager().getNextId();
			facade.createNewGame("resetTest", false, false, false);
			facade.joinGame(newID, "blue", 1);
			facade.joinGame(newID, "red", 2);
			facade.joinGame(newID, "green", 3);
			facade.joinGame(newID, "orange", 4);
			facade.buildRoad(newID, 0, new EdgeLocation(new HexLocation(2, -1), 
					EdgeDirection.NorthWest), true);
			
			//resets
			facade.resetGame(newID);
			//Checks to make sure there are no roads on map
			ModelFacade mfacade = facade.getGameManager().getGameById(newID).getModelFacade();
			assertTrue(mfacade.getModel().getMap().getRoadsOnMap().size() == 0);
		}
		catch (ServerInvalidRequestException e){
			fail();
		}
	}
	
	
	@Test
	public void robPlayer() {
		//incorrect turn phase
		try{
			setGame("discardPhase");
			facade.robPlayer(0, 1, 2, new HexLocation(0, -1), false);
			fail();
		}
		catch (ServerInvalidRequestException e) {}
		
		//not user's turn
		try{
			setGame("RobTest");
			facade.robPlayer(0, 0, 2, new HexLocation(0, -1), false);
			fail();
		}
		
		catch (ServerInvalidRequestException e) {}
		//ok test case - robber is moved, player robbed has given up resource
		try {
			setGame("RobTest");
			HexLocation loc = new HexLocation(0, -1);
			facade.robPlayer(0, 1, 2, loc, false);
			ModelFacade mfacade = facade.getGameManager().getGameById(0).getModelFacade();
			HexTile tile = mfacade.getModel().getMap().getHexTileByLocation(loc);
			assertTrue(tile.hasRobber());
		} catch (ServerInvalidRequestException e) {
			fail();
		}
	}
	
	@Test
	public void finishTurn() {
		//ok test case, player index is updated
		try{
			setGame("EndTurn");
			facade.finishTurn(0, 1);
			ModelFacade mfacade = facade.getGameManager().getGameById(0).getModelFacade();
			assertTrue(mfacade.turnManager().currentTurnPhase() == TurnPhase.ROLLING);
		}
		catch (ServerInvalidRequestException e) {
				fail();
		}
	}
	
	@Test
	public void buyDevCard() {
		//ok test case, user gains a dev card in newDevCards and loses resources
		try{
			setGame("EndTurn");
			ModelFacade mfacade = facade.getGameManager().getGameById(0).getModelFacade();
			User user = mfacade.turnManager().getUserFromIndex(1);
			int resourceSize = user.getResourceCards().getAllResourceCards().size();
			facade.buyDevCard(0, 1);
			
			assertTrue(user.getNewDevCards().size() == 1);
			assertTrue(user.getResourceCards().getAllResourceCards().size() == resourceSize-3);
		}
		catch (ServerInvalidRequestException e) {
			fail();
		}
	}
	
	@Test
	public void yearOfPlenty() {
		//ok test case, user uses card and gets two cards from bank
		try{
			setGame("YoP");
			ModelFacade mfacade = facade.getGameManager().getGameById(0).getModelFacade();
			User user = mfacade.turnManager().getUserFromIndex(1);
			int oldbrick = user.getBrickCards();
			int oldore = user.getOreCards();
			facade.playYearOfPlenty(0, 1, ResourceType.BRICK, ResourceType.ORE);
			
			user = mfacade.turnManager().getUserFromIndex(1);
			assertTrue(user.getResourceCards().getCountByType(ResourceType.BRICK) == oldbrick +1);
			assertTrue(user.getResourceCards().getCountByType(ResourceType.ORE) == oldore+1);
		}
		catch (ServerInvalidRequestException e) {
			fail();
		}
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
		//tradeOffer should exist in model after trade is offered
		try {
			setGame("offerTrade");
			User user = facade.getGameManager().getGameById(0).getModelFacade().turnManager().getUserFromIndex(2);
			ResourceCardDeck sendDeck = new ResourceCardDeck();
			sendDeck.addResourceCard(new ResourceCard(ResourceType.ORE));
			ResourceCardDeck receiverDeck = new ResourceCardDeck();
			receiverDeck.addResourceCard(new ResourceCard(ResourceType.SHEEP));
			facade.offerTrade(0, 2, 0, sendDeck, receiverDeck);
			assertTrue(facade.getGameManager().getGameById(0).getModelFacade().getModel().getTradeOffer() != null);
		} catch (ServerInvalidRequestException e) {
			e.printStackTrace();
			fail("offer trade: shouldn't have failed");
		}
	}
	
	@Test
	public void acceptTrade() {
		//user accepts the trade, resources for both players should be updated, trade offer removed
		try {
			setGame("acceptTrade");
			User offerer = facade.getGameManager().getGameById(0).getModelFacade().turnManager().getUserFromIndex(2);
			User receiver = facade.getGameManager().getGameById(0).getModelFacade().turnManager().getUserFromIndex(0);
			facade.acceptTrade(0, 0, true);
			
			assertTrue(offerer.getResourceCards().getCountByType(ResourceType.ORE) == 1);
			assertTrue(offerer.getResourceCards().getCountByType(ResourceType.SHEEP) == 2);
			assertTrue(receiver.getResourceCards().getCountByType(ResourceType.ORE) == 1);
			assertTrue(receiver.getResourceCards().getCountByType(ResourceType.SHEEP) == 0);
		} catch (ServerInvalidRequestException e) {
			e.printStackTrace();
			fail("acceptTrade: should've passed");
		}
		//user doesn't accept the trade, trade offer removed
		try {
			setGame("acceptTrade");
			facade.acceptTrade(0, 0, false);
			
			assertTrue(facade.getGameManager().getGameById(0).getModelFacade().getModel().getTradeOffer() == null);
			
		} catch (ServerInvalidRequestException e) {
			e.printStackTrace();
			fail("acceptTrade: should've passed");
		}
		
	}
	
	@Test
	public void maritimeTrade() {
		
		//regular maritime trade
		try {
			setGame("maritime");
			User user = facade.getGameManager().getGameById(0).getModelFacade().turnManager().getUserFromIndex(2);
			ResourceCardDeck bankResources = facade.getGameManager().getGameById(0).getModelFacade().bank().getResourceDeck();
			int initWood = user.getResourceCards().getCountByType(ResourceType.WOOD);
			int initSheep = user.getResourceCards().getCountByType(ResourceType.SHEEP);
			int initBankSheep = bankResources.getCountByType(ResourceType.SHEEP);
			int initBankWood = bankResources.getCountByType(ResourceType.WOOD);
			facade.maritimeTrade(0, 2, 4, ResourceType.WOOD, ResourceType.SHEEP);
			//user resources updated
			assertTrue(user.getResourceCards().getCountByType(ResourceType.WOOD) == initWood-4);
			assertTrue(user.getResourceCards().getCountByType(ResourceType.SHEEP) == initSheep + 1);
			//bank resources updated
			assertTrue(bankResources.getCountByType(ResourceType.SHEEP) == initBankSheep - 1);
			assertTrue(bankResources.getCountByType(ResourceType.WOOD) == initBankWood + 4);
		} catch (ServerInvalidRequestException e2) {
			e2.printStackTrace();
			fail("maritime, should've passed");
		}
		//has 3 port
		try {
			setGame("maritime3");
			User user = facade.getGameManager().getGameById(0).getModelFacade().turnManager().getUserFromIndex(0);
			ResourceCardDeck bankResources = facade.getGameManager().getGameById(0).getModelFacade().bank().getResourceDeck();
			int initWood = user.getResourceCards().getCountByType(ResourceType.WOOD);
			int initSheep = user.getResourceCards().getCountByType(ResourceType.SHEEP);
			int initBankSheep = bankResources.getCountByType(ResourceType.SHEEP);
			int initBankWood = bankResources.getCountByType(ResourceType.WOOD);
			facade.maritimeTrade(0, 0, 3, ResourceType.WOOD, ResourceType.SHEEP);
			//user resources updated
			assertTrue(user.getResourceCards().getCountByType(ResourceType.WOOD) == initWood-3);
			assertTrue(user.getResourceCards().getCountByType(ResourceType.SHEEP) == initSheep + 1);
			//bank resources updated
			assertTrue(bankResources.getCountByType(ResourceType.SHEEP) == initBankSheep - 1);
			assertTrue(bankResources.getCountByType(ResourceType.WOOD) == initBankWood + 3);
		} catch (ServerInvalidRequestException e1) {
			e1.printStackTrace();
			fail("maritime, should've passed"); 
		}
		//has 2 port
		try {
			setGame("maritime2");
			User user = facade.getGameManager().getGameById(0).getModelFacade().turnManager().getUserFromIndex(1);
			ResourceCardDeck bankResources = facade.getGameManager().getGameById(0).getModelFacade().bank().getResourceDeck();
			int initWood = user.getResourceCards().getCountByType(ResourceType.WOOD);
			int initSheep = user.getResourceCards().getCountByType(ResourceType.SHEEP);
			int initBankSheep = bankResources.getCountByType(ResourceType.SHEEP);
			int initBankWood = bankResources.getCountByType(ResourceType.WOOD);
			facade.maritimeTrade(0, 1, 2, ResourceType.WOOD, ResourceType.SHEEP);
			//user resources updated
			assertTrue(user.getResourceCards().getCountByType(ResourceType.WOOD) == initWood-2);
			assertTrue(user.getResourceCards().getCountByType(ResourceType.SHEEP) == initSheep + 1);
			//bank resources updated
			assertTrue(bankResources.getCountByType(ResourceType.SHEEP) == initBankSheep - 1);
			assertTrue(bankResources.getCountByType(ResourceType.WOOD) == initBankWood + 2);
		} catch (ServerInvalidRequestException e) {
			e.printStackTrace();
			fail("maritime, should've passed");
		}
		
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
			setGame("discardPhase");
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
			setGame("discardPhase");
			ArrayList<ResourceCard> resources = new ArrayList<ResourceCard>();
			resources.add(new ResourceCard(ResourceType.BRICK));
			resources.add(new ResourceCard(ResourceType.ORE));
			resources.add(new ResourceCard(ResourceType.ORE));
			resources.add(new ResourceCard(ResourceType.ORE));
			ResourceCardDeck resourcesToDiscard = new ResourceCardDeck(resources);
			facade.discardCards(0, 0, resourcesToDiscard);
//			String expectedJsonStr = extractJson("samDiscardedResult").getAsJsonObject().toString();
//			String actualJsonStr = facade.getGameManager().getGameById(0).getModelFacade().getModel().serialize().getAsJsonObject().toString();
//			assertEquals(expectedJsonStr, actualJsonStr);
			User user = facade.getGameManager().getGameById(0).getModelFacade().turnManager().getUserFromIndex(0);
			assertTrue(user.getResourceCards().getCountByType(ResourceType.BRICK) == 0);
			assertTrue(user.getResourceCards().getCountByType(ResourceType.ORE) == 0);
			assertTrue(user.getHasDiscarded() == true);
			//status changes to robbing if last one to discard...
		} catch (ServerInvalidRequestException e) {
			e.printStackTrace();
			fail("discard: shouldn't have failed");
		}
	}
}
