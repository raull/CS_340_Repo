package server;

import org.junit.*;

import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.command.game.*;

public class CommandTester {
	private ServerCommand command;
	
	public CommandTester(){
		
	}
	
	@Before
	public void setUp(){
		HttpExchange http;//do something?
		
	}
	
	@Test
	public void createGame(){
		command = new GameCreateCommand(null);
	}
	
	@Test
	public void joinGame(){
		command = new GameJoinCommand(null);
	}
	//not writing in the rest yet because I'm not sure if we want to test our commands this way or just call the ServerFacade methods instead
}
