package server;

import static org.junit.Assert.*;

import org.junit.*;

import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.command.game.*;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;

public class ServerFacadeTester {
	private ServerFacade facade;
	
	public ServerFacadeTester(){
		facade = ServerFacade.instance();
	}
	
	@Before
	public void setUp(){
		
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
	public void joinGame(){
		//without logging in first
		
		//log in, valid request
		
		//nonexistent game
		
		//color already chosen
	}
	
	
	@Test
	public void buildSettlement() {
		
	}
	
	@Test
	public void buildCity() {
		
	}
	
	@Test
	public void offerTrade() {
		
	}
	
	@Test
	public void acceptTrade() {
		
	}
	
	@Test
	public void maritimeTrade() {
		//wrong turn phase
		//not user's turn
		//no necessary resources
		//bank doesn't have card wanted
		//regular maritime trade
		//user gives 3, but doesn't have 3 port
		//user gives 2, but doesn't have 2 port
		//user has 3 port
		//user has 2 port
	}
	
	@Test
	public void discardCards() {
		//incorrect turn phase
		//user doesn't have more than 7 cards
		//user has more than 7 cards
			//trying to discard cards they don't have
			//ok test case
	}
}
