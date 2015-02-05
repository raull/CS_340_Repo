import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import shared.definitions.ResourceType;
import shared.model.*;
import shared.model.cards.ResourceCard;
import shared.model.facade.ModelFacade;
import shared.model.game.TurnManager;
import shared.model.game.TurnPhase;
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
	private Model testModel = new Model();
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
		//not user's turn
		//not in rolling phase
		
		//true test case
	}
	
	@Test
	public void testCanBuyPiece()
	{
		//not user's turn
		//can't buy piece (one for each type of piece)
		
		//true test case
	}
	
	@Test
	public void testCanPlaceRoadAtLoc()
	{
		//location is occupied
		//not connected to other pieces of the given player
		//not user's turn
		
		//true test case
	}
	
	@Test
	public void testCanBuyRoad()
	{
		//not user's turn
		//wrong turn phase
		//set up round
		//insufficient funds
		
		//true test case
		
	}
	
	@Test
	public void testCanPlaceBuildingAtLoc()
	{
		//not user's turn
		//wrong turn phase
		//set up round
		//invalid location (occupied, one away occupied)
		//test for city (must have settlement already)
		
		//true test case
	}
	
	@Test
	public void testCanBuyBuilding()
	{
		//not user's turn
		//wrong turn phase
		//insufficient funds
		
		//true test case
	}
	
	@Test
	public void testCanBuyDevCard()
	{
		//not user's turn
		//wrong turn phase
		//dev card deck empty
		//insufficient funds
		
		//true test case
	}
	
	@Test
	public void testCanPlayDevCard()
	{
		//not user's turn
		//user doesn't have a dev card
		//wrong turn phase
		//bought it this turn
		//already played dev card this turn
		//special case for monument
		
		//true test case
	}
	
	//TESTS for specific cards
	
	@Test
	public void testCanRobPlayer() 
	{
		//not user's turn
		//wrong turn phase
		//victim has no resource cards
		//victim must have city or settlement connected to tile robber is on
		
		//true test case		
	}
	
//	@Test
//	public void testCanPlaceRobber()
//	{
//		//robber already there
//		
//		//true test case
//	}
	
	@Test
	public void testCanOfferTrade() 
	{
		//not user's turn
		//wrong turn phase
		
		//I don't quite understand the other test cases here
		
		//true test case
	}
	
	@Test
	public void testCanAcceptTrade() 
	{
		//hasn't been offered a trade
		//doesn't have the cards required for trade
		
		//true test case
	}
	
	@Test
	public void testCanMaritimeTrade() 
	{
		//user has necessary resources
		//bank has necessary resources
		//not user's turn
		//tests with different ratios
		
		
	}
	
	@Test
	public void testCanFinishTurn() 
	{
		//not user's turn
		//wrong turn phase
		
		//true test case
	}

}