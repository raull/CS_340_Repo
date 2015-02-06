import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import client.poller.Poller;
import shared.definitions.DevCardType;
import shared.definitions.HexType;
import shared.definitions.ResourceType;
import shared.locations.HexLocation;
import shared.model.Model;
import shared.model.board.HexTile;
import shared.model.board.Map;
import shared.model.cards.Bank;
import shared.model.cards.DevCardDeck;
import shared.model.cards.ResourceCardDeck;
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
		Bank bank = facade.bank();
		
		//verify dev deck contents
		DevCardDeck devdeck = bank.getDevCardDeck();
		assertEquals(devdeck.getCountByType(DevCardType.MONOPOLY),2);
		assertEquals(devdeck.getCountByType(DevCardType.MONUMENT),5);
		assertEquals(devdeck.getCountByType(DevCardType.ROAD_BUILD),2);
		assertEquals(devdeck.getCountByType(DevCardType.SOLDIER),14);
		assertEquals(devdeck.getCountByType(DevCardType.YEAR_OF_PLENTY),2);
		
		//verify hextile types and numbers
		HexTile tile = map.getHexTileByLocation(new HexLocation(0,-2));
		assertEquals(tile.getType(), HexType.WOOD);
		assertEquals(tile.getNumber(), 11);
		
		tile = map.getHexTileByLocation(new HexLocation(1,-2));
		assertEquals(tile.getType(), HexType.SHEEP);
		assertEquals(tile.getNumber(), 10);

		tile = map.getHexTileByLocation(new HexLocation(2,-2));
		assertEquals(tile.getType(), HexType.BRICK);
		assertEquals(tile.getNumber(), 5);
		
		tile = map.getHexTileByLocation(new HexLocation(-1,-1));
		assertEquals(tile.getType(), HexType.WOOD);
		assertEquals(tile.getNumber(), 3);

		tile = map.getHexTileByLocation(new HexLocation(0, -1));
		assertEquals(tile.getType(), HexType.SHEEP);
		assertEquals(tile.getNumber(), 9);

		tile = map.getHexTileByLocation(new HexLocation(1, -1));
		assertEquals(tile.getType(), HexType.WOOD);
		assertEquals(tile.getNumber(), 4);

		tile = map.getHexTileByLocation(new HexLocation(2, -1));
		assertEquals(tile.getType(), HexType.WHEAT);
		assertEquals(tile.getNumber(), 6);

		tile = map.getHexTileByLocation(new HexLocation(-2, 0));
		assertEquals(tile.getType(), HexType.ORE);
		assertEquals(tile.getNumber(), 3);
		
		tile = map.getHexTileByLocation(new HexLocation(-1, 0));
		assertEquals(tile.getType(), HexType.WHEAT);
		assertEquals(tile.getNumber(), 8);

		tile = map.getHexTileByLocation(new HexLocation(0,0));
		assertEquals(tile.getType(), HexType.ORE);
		assertEquals(tile.getNumber(), 5);

		tile = map.getHexTileByLocation(new HexLocation(1, 0));
		assertEquals(tile.getType(), HexType.BRICK);
		assertEquals(tile.getNumber(), 4);

		tile = map.getHexTileByLocation(new HexLocation(2, 0));
		assertEquals(tile.getType(), HexType.SHEEP);
		assertEquals(tile.getNumber(), 10);

		tile = map.getHexTileByLocation(new HexLocation(-2,1));
		assertEquals(tile.getType(), HexType.WHEAT);
		assertEquals(tile.getNumber(), 2);

		tile = map.getHexTileByLocation(new HexLocation(-1,1));
		assertEquals(tile.getType(), HexType.WOOD);
		assertEquals(tile.getNumber(), 6);

		tile = map.getHexTileByLocation(new HexLocation(1,-2));
		assertEquals(tile.getType(), HexType.DESERT);

		tile = map.getHexTileByLocation(new HexLocation(1,1));
		assertEquals(tile.getType(), HexType.SHEEP);
		assertEquals(tile.getNumber(), 12);

		tile = map.getHexTileByLocation(new HexLocation(-2,2));
		assertEquals(tile.getType(), HexType.ORE);
		assertEquals(tile.getNumber(), 9);

		tile = map.getHexTileByLocation(new HexLocation(-1,2));
		assertEquals(tile.getType(), HexType.BRICK);
		assertEquals(tile.getNumber(), 8);

		tile = map.getHexTileByLocation(new HexLocation(0,2));
		assertEquals(tile.getType(), HexType.WHEAT);
		assertEquals(tile.getNumber(), 11);
		
		//verify no roads, settlements, or cities on map
		assertEquals(map.getCitiesOnMap().size(), 0);
		assertEquals(map.getRoadsOnMap().size(), 0);
		assertEquals(map.getSettlementsOnMap().size(), 0);
		
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
		
		//bank 24 resources each
		ResourceCardDeck deck = bank.getResourceDeck();
		assertEquals(deck.getCountByType(ResourceType.BRICK), 24);
		assertEquals(deck.getCountByType(ResourceType.ORE), 24);
		assertEquals(deck.getCountByType(ResourceType.SHEEP), 24);
		assertEquals(deck.getCountByType(ResourceType.WHEAT), 24);
		assertEquals(deck.getCountByType(ResourceType.WOOD), 24);
		
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
		
		//empty log and chat
		assertEquals(model.getLog().lines.size(), 0);
		assertEquals(model.getChat().lines.size(), 0);
	}

}