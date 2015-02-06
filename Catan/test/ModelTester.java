import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import shared.definitions.*;
import shared.locations.*;
import shared.model.*;
import shared.model.cards.Bank;
import shared.model.cards.DevCardDeck;
import shared.model.cards.ResourceCard;
import shared.model.facade.ModelFacade;
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
		
		//wrong turn phase?

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
		
		//unnecessary to test setup phase
		
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
		assertTrue(testModelFacade.canBuyPiece(turnManager, turnManager.currentUser(), PieceType.CITY));
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

	@Test
	public void testCanPlayDevCard()
	{
		//not user's turn
		assert(false);//need to fill in!!!
		//user doesn't have a dev card
		
		//bought it this turn
		//already played dev card this turn
		//special case for monument

		//true test case
	}
//
//	//TESTS for specific cards
//
//	@Test
//	public void testCanRobPlayer() 
//	{
//		//not user's turn
//		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
//		TurnManager turnManager = testModelFacade.turnManager();
//		assertFalse(testModelFacade.canRobPlayer(HEXTILE, turnManager.getUserFromIndex(1), turnManager.currentUser()));
//
//		//wrong turn phase
//		testModelFacade.updateModel(testMoxy.getModel("rollingPhase.json"));
//		TurnManager turnManager = testModelFacade.turnManager();
//		assertFalse(testModelFacade.canRobPlayer(HEXTILE, turnManager.currentUser(), VICTIM));		
//		
//		//victim has no resource cards
//		testModelFacade.updateModel(testMoxy.getModel(""));
//		TurnManager turnManager = testModelFacade.turnManager();
//		assertFalse(testModelFacade.canRobPlayer(HEXTILE, turnManager.currentUser(), VICTIM));
//		
//		//victim must have city or settlement connected to tile robber is on
//		assertFalse(testModelFacade.canRobPlayer(HEXTILE, turnManager.currentUser(), VICTIM));
//
//		//true test case		
//		assertTrue(testModelFacade.canRobPlayer(HEXTILE, turnManager.currentUser(), VICTIM));
//	}
//
//	//	@Test
//	//	public void testCanPlaceRobber()
//	//	{
//	//		//robber already there
//	//		
//	//		//true test case
//	//	}
//
//	@Test
//	public void testCanOfferTrade() 
//	{
//		//not user's turn
//		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
//		TurnManager turnManager = testModelFacade.turnManager();
//		assertFalse(testModelFacade.canOfferTrade(turnManager, turnManager.getUserFromIndex(1),
//				turnManager.currentUser(), TRADEOFFER));
//
//		//wrong turn phase
//		testModelFacade.updateModel(testMoxy.getModel("rollingPhase.json"));
//		TurnManager turnManager = testModelFacade.turnManager();
//		assertFalse(testModelFacade.canOfferTrade(turnManager, turnManager.currentUser(),
//				OTHERUSER, TRADEOFFER));
//
//		//I don't quite understand the other test cases here
//
//		//true test case
//		testModelFacade.updateModel(testMoxy.getModel(""));
//		TurnManager turnManager = testModelFacade.turnManager();
//		assertTrue(testModelFacade.canOfferTrade(turnManager, turnManager.currentUser(),
//				OTHERUSER, TRADEOFFER));
//	}
//
//	@Test
//	public void testCanAcceptTrade() 
//	{
//		//hasn't been offered a trade
//		//doesn't have the cards required for trade
//
//		//true test case
//	}
//
//	@Test
//	public void testCanMaritimeTrade() 
//	{
//		//not user's turn
//		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
//		TurnManager turnManager = testModelFacade.turnManager();
//		Bank bank = testModelFacade.bank();
//		assertFalse(testModelFacade.canMaritimeTrade(turnManager, bank, turnManager.getUserFromIndex(1), TRADEOFFER));
//
//		//wrong turn phase
//		testModelFacade.updateModel(testMoxy.getModel("rollingPhase.json"));
//		turnManager = testModelFacade.turnManager();
//		bank = testModelFacade.bank();
//		assertFalse(testModelFacade.canMaritimeTrade(turnManager, bank, turnManager.currentUser(), TRADEOFFER));
//		
//		//user doesn't have necessary resources
//		testModelFacade.updateModel(testMoxy.getModel(""));
//		turnManager = testModelFacade.turnManager();
//		bank = testModelFacade.bank();
//		assertFalse(testModelFacade.canMaritimeTrade(turnManager, bank, turnManager.currentUser(), TRADEOFFER));
//
//		//bank doesn't have necessary resources
//		testModelFacade.updateModel(testMoxy.getModel(""));
//		turnManager = testModelFacade.turnManager();
//		bank = testModelFacade.bank();
//		assertFalse(testModelFacade.canMaritimeTrade(turnManager, bank, turnManager.currentUser(), TRADEOFFER));
//
//		//tests with different ratios
//
//
//	}
//
//	@Test
//	public void testCanFinishTurn() 
//	{
//		//not user's turn
//		testModelFacade.updateModel(testMoxy.getModel("currentTurn0.json"));
//		TurnManager turnManager = testModelFacade.turnManager();
//		assertFalse(testModelFacade.canFinishTurn(turnManager, turnManager.getUserFromIndex(1)));
//
//		//wrong turn phase
//		testModelFacade.updateModel(testMoxy.getModel("rollingPhase.json"));
//		TurnManager turnManager = testModelFacade.turnManager();
//		assertFalse(testModelFacade.canFinishTurn(turnManager, turnManager.currentUser()));
//		
//		//true test case
//		testModelFacade.updateModel(testMoxy.getModel(""));
//		TurnManager turnManager = testModelFacade.turnManager();
//		assertTrue(testModelFacade.canFinishTurn(turnManager, turnManager.currentUser()));
//
//	}

}