import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import client.poller.Poller;
import shared.locations.HexLocation;
import shared.model.Model;
import shared.model.board.HexTile;
import shared.model.board.Map;
import shared.model.facade.ModelFacade;
import shared.model.game.ScoreKeeper;
import shared.model.game.TurnManager;
import shared.model.game.TurnPhase;
import shared.model.game.User;
import shared.proxy.Moxy;
import shared.proxy.Proxy;

/**
	This class runs tests on the ServerPoller class.
 */

public class PollerTester
{
	private Poller testPoller;
	
	public PollerTester()
	{

	}



	@Before
	public void setUp()
	{
		Proxy testProxy = new Moxy();
		ModelFacade testFacade = new ModelFacade();
		testPoller = new Poller(testProxy, testFacade);
	}
	/*
	 *  Tests to verify that the Poller can update the Model
		Should really only need a few tests.
		Perhaps one where the model has not changed and one where it has.
	 */
	@Test
	public void testPollServer()
	{
		testPoller.pollServer();
		
		//test that the model has indeed been initialized with the information from model.json
		ModelFacade facade = testPoller.getModelFacade();
		Map map = facade.map();
		TurnManager turnManager = facade.turnManager();
		ScoreKeeper score = facade.score();
		
		//verify deck contents
		//verify hextile types and numbers
		//verify no roads, settlements, or cities on map
		//verify port locations, types, and ratios
		
		//verify robber on 0,1
		HexTile shouldHaveRobber = map.getHexTileByLocation(new HexLocation(0,1));
		assertTrue(shouldHaveRobber.hasRobber());
		
		//player 0 has no resources, no devcards, no victorypoints,
			//playerid is 12, has not played devcard, name tiger, color red
		User player0 = turnManager.getUserFromIndex(0);
		assertTrue(player0.getColor() == Color.RED);
		assertFalse(player0.getHasPlayedDevCard());
		assertTrue(player0.getName().equals("tiger"));
		assertTrue(player0.getNewDevCards().size() == 0);
		assertTrue(player0.getUsableDevCards().size() == 0);
		assertTrue(player0.getPlayerID() == 12);
		assertTrue(player0.getResourceCards().getAllResourceCards().size() == 0);
		assertTrue(player0.ports().size() == 0);
		assertTrue(score.getScoreValue(0) == 0);
		
		//player 1 is similar except playerid 0, name sam, collor yellow
		User player1 = turnManager.getUserFromIndex(1);
		assertTrue(player0.getColor() == Color.YELLOW);
		assertFalse(player0.getHasPlayedDevCard());
		assertTrue(player0.getName().equals("Sam"));
		assertTrue(player0.getNewDevCards().size() == 0);
		assertTrue(player0.getUsableDevCards().size() == 0);
		assertTrue(player0.getPlayerID() == 0);
		assertTrue(player0.getResourceCards().getAllResourceCards().size() == 0);
		assertTrue(player0.ports().size() == 0);
		assertTrue(score.getScoreValue(1) == 0);
		
		//empty log
		
		//empty chat
		
		//bank 24 resources each
		
		//firstround
		assertTrue(turnManager.currentTurnPhase() == TurnPhase.FIRSTROUND);
		
		//current user 0
		assertTrue(turnManager.getCurrentTurn() == 0);
		
		//no longest road or largest army
		assertEquals(turnManager.getLogestRoadIndex(), -1);
		assertEquals(turnManager.getLargestArmyIndex(), -1);
		
		//no winner
		assertTrue(score.getWinner() == -1);
		
		//version 0
		Model model = facade.model;
		assertEquals(model.getVersion(), 0);
	}

}