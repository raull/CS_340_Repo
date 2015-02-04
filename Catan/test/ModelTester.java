import shared.model.*;

/**
	This class runs tests on the ModelFacade class.
*/
public class ModelTester
{
	public ModelTester()
	{
		
	}
	
	private ModelFacade testModel;
	
	//Test can initialize model from JSON from server
	
	@Test
	public void testCanDiscardCards()
	{
		//incorrect turn phase
		//not their turn??
		//doesn't have over seven card
		
		//true test case
		
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