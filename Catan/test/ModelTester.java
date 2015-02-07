import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import shared.definitions.*;
import shared.locations.*;
import shared.model.*;
import shared.model.board.HexTile;
import shared.model.cards.Bank;
import shared.model.cards.DevCard;
import shared.model.cards.DevCardDeck;
import shared.model.cards.ResourceCard;
import shared.model.cards.ResourceCardDeck;
import shared.model.facade.ModelFacade;
import shared.model.game.TradeOffer;
import shared.model.game.TurnManager;
import shared.model.game.User;
import shared.proxy.Moxy;

/**
	This class runs tests on the ModelFacade class.
 */
public class ModelTester
{
	public ModelTester()
	{

	}

	private ModelFacade testModelFacade = new ModelFacade();
	private Moxy testMoxy = new Moxy();

	//Test can initialize model from JSON from server

	@Test
	public void testCanDiscardCards()
	{
		//not the correct turn phase
		testModelFacade.updateModel(testMoxy.getModel("discardWrongPhase.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		User currentUser = turnManager.currentUser();
		assertFalse(testModelFacade.canDiscardCards(testModelFacade.turnManager(), currentUser, new ArrayList<ResourceCard>()));

		//doesn't have over seven cards
		testModelFacade.updateModel(testMoxy.getModel("discardTooFewCards.json"));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		assertFalse(testModelFacade.canDiscardCards(testModelFacade.turnManager(), currentUser, new ArrayList<ResourceCard>()));

		//not their turn??
		testModelFacade.updateModel(testMoxy.getModel("discard.json")); //correct JSON
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		User notCurrentUser = turnManager.getUserFromIndex((currentUser.getTurnIndex() +1)%turnManager.getUsers().size());
		ArrayList<ResourceCard> discard = new ArrayList<ResourceCard>();
		discard.add(new ResourceCard(ResourceType.BRICK));
		assertFalse(testModelFacade.canDiscardCards(testModelFacade.turnManager(), notCurrentUser, discard));

		//true test case
		assertTrue(testModelFacade.canDiscardCards(testModelFacade.turnManager(), currentUser, discard));	
	}

	@Test
	public void testCanRollNumber()
	{
		//not in rolling phase
		testModelFacade.updateModel(testMoxy.getModel("rollNumWrongPhase.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canRollNumber(turnManager, turnManager.currentUser()));

		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel("rollNum.json"));
		turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canRollNumber(turnManager, turnManager.getUserFromIndex(1))); //in this JSON, the currentUser is index 0

		//true test case
		assertTrue(testModelFacade.canRollNumber(turnManager, turnManager.currentUser()));
	}

	@Test
	public void testCanBuyPiece()
	{
		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canBuyPiece(turnManager, turnManager.getUserFromIndex(1), PieceType.ROAD));

		//can't buy piece (one for each type of piece)
		testModelFacade.updateModel(testMoxy.getModel("cantBuyAnyPiece.json"));
		turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canBuyPiece(turnManager, turnManager.currentUser(), PieceType.ROAD));
		assertFalse(testModelFacade.canBuyPiece(turnManager, turnManager.currentUser(), PieceType.CITY));
		assertFalse(testModelFacade.canBuyPiece(turnManager, turnManager.currentUser(), PieceType.SETTLEMENT));

		//true test case
		testModelFacade.updateModel(testMoxy.getModel("canBuyRoad.json"));
		turnManager = testModelFacade.turnManager();
		assertTrue(testModelFacade.canBuyPiece(turnManager, turnManager.currentUser(), PieceType.ROAD));
	}

	@Test
	public void testCanPlaceRoadAtLoc()
	{
		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canPlaceRoadAtLoc(turnManager, new EdgeLocation(new HexLocation(0,0), EdgeDirection.NorthWest), turnManager.getUserFromIndex(1)));

		//location is occupied
		testModelFacade.updateModel(testMoxy.getModel("cantRoadatX0Y1S.json"));
		turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canPlaceRoadAtLoc(turnManager, new EdgeLocation(new HexLocation(0,1), EdgeDirection.South), turnManager.currentUser()));

		//not connected to other pieces of the given player
		assertFalse(testModelFacade.canPlaceRoadAtLoc(turnManager, new EdgeLocation(new HexLocation(-1,-1), EdgeDirection.South), turnManager.currentUser()));

		//true test case
		assertTrue(testModelFacade.canPlaceRoadAtLoc(turnManager, new EdgeLocation(new HexLocation(0,1), EdgeDirection.SouthEast), turnManager.currentUser()));
	}

	@Test
	public void testCanBuyRoad()
	{
		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canBuyPiece(turnManager,  turnManager.getUserFromIndex(1), PieceType.ROAD));

		//wrong turn phase
		testModelFacade.updateModel(testMoxy.getModel("rollingPhase.json"));
		turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canBuyPiece(turnManager, turnManager.currentUser(), PieceType.ROAD));

		//insufficient funds
		testModelFacade.updateModel(testMoxy.getModel("cantBuyAnyPiece.json"));
		turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canBuyPiece(turnManager, turnManager.currentUser(), PieceType.ROAD));

		//true test case
		testModelFacade.updateModel(testMoxy.getModel("canBuyRoad.json"));
		turnManager = testModelFacade.turnManager();
		assertTrue(testModelFacade.canBuyPiece(turnManager, turnManager.currentUser(), PieceType.ROAD));
	}

	@Test
	public void testCanPlaceBuildingAtLoc()
	{
		//wrong turn phase
		testModelFacade.updateModel(testMoxy.getModel("finishTurn.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		VertexLocation validLocation = new VertexLocation(new HexLocation(2,2),VertexDirection.SouthWest);
		VertexLocation invalidLocation = new VertexLocation(new HexLocation(-1,-1), VertexDirection.West);
		VertexLocation validCityLocationForUser0 = new VertexLocation(new HexLocation(1,-1), VertexDirection.SouthEast);

		User currentUser = turnManager.currentUser();
		assertFalse(testModelFacade.canPlaceBuildingAtLoc(turnManager, validLocation, turnManager.currentUser(), PieceType.SETTLEMENT));

		//set up round
		testModelFacade.updateModel(testMoxy.getModel("model.json"));
		turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canPlaceBuildingAtLoc(turnManager, validLocation, currentUser, PieceType.SETTLEMENT));

		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
		turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canPlaceBuildingAtLoc(turnManager, validLocation, turnManager.getUserFromIndex(1), PieceType.SETTLEMENT));

		//invalid location (occupied, one away occupied)
		turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canPlaceBuildingAtLoc(turnManager, invalidLocation, turnManager.currentUser(), PieceType.SETTLEMENT));		

		//test for city (must have settlement already)
		assertFalse(testModelFacade.canPlaceBuildingAtLoc(turnManager, validLocation, turnManager.currentUser(), PieceType.CITY));
		assertTrue(testModelFacade.canPlaceBuildingAtLoc(turnManager, validCityLocationForUser0, turnManager.currentUser(), PieceType.CITY));

		//true test case
		assertTrue(testModelFacade.canPlaceBuildingAtLoc(turnManager, validLocation, turnManager.currentUser(), PieceType.SETTLEMENT));
	}

	@Test
	public void testCanBuyBuilding()
	{
		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canBuyPiece(turnManager, turnManager.getUserFromIndex(1), PieceType.CITY));

		//wrong turn phase
		testModelFacade.updateModel(testMoxy.getModel("rollingPhase.json"));
		turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canBuyPiece(turnManager, turnManager.currentUser(), PieceType.CITY));

		//insufficient funds
		testModelFacade.updateModel(testMoxy.getModel("cantBuyAnyPiece.json"));
		turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canBuyPiece(turnManager, turnManager.currentUser(), PieceType.SETTLEMENT));

		//true test case
		testModelFacade.updateModel(testMoxy.getModel("buyCityValidBuy.json"));
		turnManager = testModelFacade.turnManager();
		assertTrue(testModelFacade.canBuyPiece(turnManager, turnManager.currentUser(), PieceType.SETTLEMENT));
	}

	@Test
	public void testCanBuyDevCard()
	{
		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		Bank bank = testModelFacade.bank();
		DevCardDeck devDeck = bank.getDevCardDeck();
		assertFalse(testModelFacade.canBuyDevCard(turnManager, turnManager.getUserFromIndex(1), devDeck));

		//wrong turn phase
		testModelFacade.updateModel(testMoxy.getModel("rollingPhase.json"));
		turnManager = testModelFacade.turnManager();
		bank = testModelFacade.bank();
		devDeck = bank.getDevCardDeck();
		assertFalse(testModelFacade.canBuyDevCard(turnManager, turnManager.currentUser(), devDeck));

		//dev card deck empty
		testModelFacade.updateModel(testMoxy.getModel("buydevNoDevLeft.json"));
		turnManager = testModelFacade.turnManager();
		bank = testModelFacade.bank();
		devDeck = bank.getDevCardDeck();
		assertFalse(testModelFacade.canBuyDevCard(turnManager, turnManager.currentUser(), devDeck));

		//insufficient funds
		testModelFacade.updateModel(testMoxy.getModel("cantBuyAnyPiece.json"));
		turnManager = testModelFacade.turnManager();
		bank = testModelFacade.bank();
		devDeck = bank.getDevCardDeck();
		assertFalse(testModelFacade.canBuyDevCard(turnManager, turnManager.currentUser(), devDeck));

		//true test case
		testModelFacade.updateModel(testMoxy.getModel("buydevValidBuy.json"));
		turnManager = testModelFacade.turnManager();
		bank = testModelFacade.bank();
		devDeck = bank.getDevCardDeck();
		assertTrue(testModelFacade.canBuyDevCard(turnManager, turnManager.currentUser(), devDeck));
	}
	
//TODO replace this with tests for each individual card type
//	@Test
//	public void testCanPlayDevCard()
//	{
//		//already played dev card this turn
//		testModelFacade.updateModel(testMoxy.getModel("alreadyPlayedDev.json"));
//		TurnManager turnManager = testModelFacade.turnManager();
//		DevCard devCard = new DevCard(DevCardType.SOLDIER);
//		assertFalse(testModelFacade.canPlayDevCard(turnManager, turnManager.currentUser(), devCard));
//		
//		//bought it this turn
//		testModelFacade.updateModel(testMoxy.getModel("boughtRdBldgThisTurn.json"));
//		devCard = new DevCard(DevCardType.ROAD_BUILD);
//		turnManager = testModelFacade.turnManager();
//		assertFalse(testModelFacade.canPlayDevCard(turnManager, turnManager.currentUser(), devCard));
//		
//		//user doesn't have a dev card
//		testModelFacade.updateModel(testMoxy.getModel("user0NoDev.json"));
//		turnManager = testModelFacade.turnManager();
//		assertFalse(testModelFacade.canPlayDevCard(turnManager, turnManager.currentUser(), devCard));
//
//		//not user's turn
//		testModelFacade.updateModel(testMoxy.getModel("canPlaySoldier.json"));
//		turnManager = testModelFacade.turnManager();
//		devCard = new DevCard(DevCardType.SOLDIER);
//		assertFalse(testModelFacade.canPlayDevCard(turnManager, turnManager.getUserFromIndex(1), devCard));
//		
//		//true test case
//		assertTrue(testModelFacade.canPlayDevCard(turnManager, turnManager.currentUser(), devCard));
//	}
	
	@Test
	public void testCanPlaySoldier()
	{
		//already played dev card this turn
		testModelFacade.updateModel(testMoxy.getModel("alreadyPlayedDev.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		User currentUser = turnManager.currentUser();
		User validVictim = turnManager.getUserFromIndex(1);
		HexTile validHex = new HexTile(HexType.WOOD, new HexLocation(1,0), 11);
		DevCard devCard = new DevCard(DevCardType.SOLDIER);
		assertFalse(testModelFacade.canPlaySoldier(turnManager, currentUser, validVictim, validHex));
		
		//bought it this turn
		testModelFacade.updateModel(testMoxy.getModel("boughtAllDevCardsThisTurn.json"));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		validVictim = turnManager.getUserFromIndex(1);
		validHex = new HexTile(HexType.WOOD, new HexLocation(1,0), 11);
		devCard = new DevCard(DevCardType.SOLDIER);
		assertFalse(testModelFacade.canPlaySoldier(turnManager, currentUser, validVictim, validHex));
		
		//user doesn't have a dev card
		testModelFacade.updateModel(testMoxy.getModel("user0NoDev.json"));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		validVictim = turnManager.getUserFromIndex(1);
		validHex = new HexTile(HexType.WOOD, new HexLocation(1,0), 11);
		devCard = new DevCard(DevCardType.SOLDIER);
		assertFalse(testModelFacade.canPlaySoldier(turnManager, currentUser, validVictim, validHex));

		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel("canPlaySoldier.json"));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		validVictim = turnManager.getUserFromIndex(1);
		validHex = new HexTile(HexType.WOOD, new HexLocation(1,0), 11);
		devCard = new DevCard(DevCardType.SOLDIER);
		assertFalse(testModelFacade.canPlaySoldier(turnManager, validVictim, currentUser, validHex)); //note the users are switched here
		
		//true test case
		assertFalse(testModelFacade.canPlaySoldier(turnManager, currentUser, validVictim, validHex));
	}
	
	@Test
	public void testCanPlayMonument()
	{
		//bought it this turn
		testModelFacade.updateModel(testMoxy.getModel("boughtAllDevCardsThisTurn.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		User currentUser = turnManager.currentUser();
		assertFalse(testModelFacade.canPlayMonument(turnManager, currentUser));
		
		//user doesn't have a dev card
		testModelFacade.updateModel(testMoxy.getModel("user0NoDev.json"));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		assertFalse(testModelFacade.canPlayMonument(turnManager, currentUser));

		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel("canPlayMonument.json"));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		assertFalse(testModelFacade.canPlayMonument(turnManager, turnManager.getUserFromIndex(1)));
		
		//true test case
		assertTrue(testModelFacade.canPlayMonument(turnManager, currentUser));
	}
	
	@Test
	public void testCanPlayMonopoly()
	{
		//already played dev card this turn
		testModelFacade.updateModel(testMoxy.getModel("alreadyPlayedDev.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		User currentUser = turnManager.currentUser();
		
		//bought it this turn
		testModelFacade.updateModel(testMoxy.getModel("boughtAllDevCardsThisTurn.json"));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		
		//user doesn't have a dev card
		testModelFacade.updateModel(testMoxy.getModel("user0NoDev.json"));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();

		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel(""/*REPLACE WITH YOUR JSON*/));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		
		//true test case
	}
	
	@Test
	public void testCanPlayYearOfPlenty()
	{
		//already played dev card this turn
		testModelFacade.updateModel(testMoxy.getModel("alreadyPlayedDev.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		User currentUser = turnManager.currentUser();
		
		//bought it this turn
		testModelFacade.updateModel(testMoxy.getModel("boughtAllDevCardsThisTurn.json"));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		
		//user doesn't have a dev card
		testModelFacade.updateModel(testMoxy.getModel("user0NoDev.json"));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();

		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel(""/*REPLACE WITH YOUR JSON*/));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		
		//true test case
	}
	
	@Test
	public void testCanPlayRoadBuilding()
	{
		//already played dev card this turn
		testModelFacade.updateModel(testMoxy.getModel("alreadyPlayedDev.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		User currentUser = turnManager.currentUser();
		
		//bought it this turn
		testModelFacade.updateModel(testMoxy.getModel("boughtAllDevCardsThisTurn.json"));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		
		//user doesn't have a dev card
		testModelFacade.updateModel(testMoxy.getModel("user0NoDev.json"));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();

		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel(""/*REPLACE WITH YOUR JSON*/));
		turnManager = testModelFacade.turnManager();
		currentUser = turnManager.currentUser();
		
		//true test case
	}

	@Test
	public void testCanRobPlayer() 
	{
		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		HexLocation location = new HexLocation(0,0);
		HexTile tile = testModelFacade.map().getHexTileByLocation(location);
		assertFalse(testModelFacade.canRobPlayer(tile, turnManager.getUserFromIndex(1), turnManager.currentUser()));

		//wrong turn phase
		testModelFacade.updateModel(testMoxy.getModel("rollingPhase.json"));
		turnManager = testModelFacade.turnManager();
		tile = testModelFacade.map().getHexTileByLocation(location);
		assertFalse(testModelFacade.canRobPlayer(tile, turnManager.currentUser(), turnManager.getUserFromIndex(1)));		

		//victim has no resource cards
		testModelFacade.updateModel(testMoxy.getModel("user2NoResources.json"));
		turnManager = testModelFacade.turnManager();
		location = new HexLocation(-2,0);
		tile =testModelFacade.map().getHexTileByLocation(location);
		assertFalse(testModelFacade.canRobPlayer(tile, turnManager.currentUser(), turnManager.getUserFromIndex(2)));

		//victim must have city or settlement connected to tile robber is on
		location = new HexLocation(0,2);
		tile = testModelFacade.map().getHexTileByLocation(location);
		assertFalse(testModelFacade.canRobPlayer(tile, turnManager.currentUser(), turnManager.getUserFromIndex(1)));

		//Robber is not set to a new location 
		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
		turnManager = testModelFacade.turnManager();
		location = new HexLocation(1,1);
		tile = testModelFacade.map().getHexTileByLocation(location);
		assertFalse(testModelFacade.canRobPlayer(tile, turnManager.currentUser(), turnManager.getUserFromIndex(1)));
		
		// True case
		//Setting a Hex that doesn't have the Robber
		location = new HexLocation(0, 1);
		tile = testModelFacade.map().getHexTileByLocation(location);
		assertTrue(testModelFacade.canRobPlayer(tile, turnManager.currentUser(), turnManager.getUserFromIndex(0)));
	}

	
	@Test
	public void testCanOfferTrade() 
	{
		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canOfferTrade(turnManager, turnManager.getUserFromIndex(1),
				turnManager.currentUser(), null));

		//wrong turn phase
		testModelFacade.updateModel(testMoxy.getModel("rollingPhase.json"));
		turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canOfferTrade(turnManager, turnManager.currentUser(),
				turnManager.getUserFromIndex(1), null));

		//Trading with itself
		assertFalse(testModelFacade.canOfferTrade(turnManager, turnManager.currentUser(),
				turnManager.currentUser(), null));
		
		//A Player doesn't have enough resources
		testModelFacade.updateModel(testMoxy.getModel("user2NoResources.json"));
		turnManager = testModelFacade.turnManager();
		TradeOffer offer = new TradeOffer(new ResourceCardDeck(new ResourceType[]{ResourceType.BRICK}), new ResourceCardDeck(new ResourceType[]{ResourceType.ORE}));
		assertFalse(testModelFacade.canOfferTrade(turnManager, turnManager.currentUser(),
				turnManager.getUserFromIndex(2), offer));
		

		//true test case
		testModelFacade.updateModel(testMoxy.getModel("offerTrade.json"));
		turnManager = testModelFacade.turnManager();
		
		User sender = turnManager.getUserFromIndex(testModelFacade.model.tradeOffer.getSenderIndex());
		User receiver = turnManager.getUserFromIndex(testModelFacade.model.tradeOffer.getReceiverIndex());
		
		assertTrue(testModelFacade.canOfferTrade(turnManager, sender, receiver, testModelFacade.model.tradeOffer));
	}
	//	@Test
	//	public void testCanAcceptTrade() 
	//	{
	//		//hasn't been offered a trade
	//		//doesn't have the cards required for trade
	//
	//		//true test case
	//	}
	//
//		@Test
//		public void testCanMaritimeTrade() 
//		{
//			//wrong turn phase
//			testModelFacade.updateModel(testMoxy.getModel("rollingPhase.json"));
//			
//			TurnManager turnManager = testModelFacade.turnManager();
//			Bank bank = testModelFacade.bank();
//			ArrayList<ResourceCard> empty = new ArrayList<ResourceCard>();
//			ResourceCard card = new ResourceCard(ResourceType.BRICK);
//			ResourceCardDeck deck = new ResourceCardDeck(empty);
//			TradeOffer offer = new TradeOffer(4, deck, deck);
//			
//			assertFalse(testModelFacade.canMaritimeTrade(turnManager, bank, turnManager.currentUser(), offer));
//	
//			//not user's turn
//			testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
//			turnManager = testModelFacade.turnManager();
//			bank = testModelFacade.bank();
//			assertFalse(testModelFacade.canMaritimeTrade(turnManager, bank, turnManager.getUserFromIndex(1), offer));
//
//			
//			//user doesn't have necessary resources
//			testModelFacade.updateModel(testMoxy.getModel(""));
//			turnManager = testModelFacade.turnManager();
//			bank = testModelFacade.bank();
//			assertFalse(testModelFacade.canMaritimeTrade(turnManager, bank, turnManager.currentUser(), offer));
//	
//			//bank doesn't have necessary resources
//			testModelFacade.updateModel(testMoxy.getModel(""));
//			turnManager = testModelFacade.turnManager();
//			bank = testModelFacade.bank();
//			assertFalse(testModelFacade.canMaritimeTrade(turnManager, bank, turnManager.currentUser(), offer));
//	
//			//tests with different ratios
//	
//	
//		}
	
	@Test
	public void testCanFinishTurn() 
	{
		//wrong turn phase
		testModelFacade.updateModel(testMoxy.getModel("rollingPhase.json"));
		TurnManager turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canFinishTurn(turnManager, turnManager.currentUser()));

		//not user's turn
		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
		turnManager = testModelFacade.turnManager();
		assertFalse(testModelFacade.canFinishTurn(turnManager, turnManager.getUserFromIndex(1)));

		//true test case
		assertTrue(testModelFacade.canFinishTurn(turnManager, turnManager.currentUser()));

	}

}