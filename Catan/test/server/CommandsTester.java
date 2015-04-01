package server;

import org.junit.Before;
import org.junit.Test;

import server.facade.ServerFacade;
import server.handler.Handler;
import shared.proxy.ProxyException;
import shared.proxy.ServerProxy;
import shared.proxy.games.JoinGameRequest;
import shared.proxy.user.Credentials;

public class CommandsTester {
	private Handler handler;
	
	public CommandsTester(){
		handler = new Handler(false);
	}
	private String host = "localhost";
	private String port = "8081";
	private ServerProxy testProxy = new ServerProxy(host, port);
	
	@Before
	public void setUp() throws ProxyException{
		testProxy.login(new Credentials("Sam", "sam"));
		testProxy.join(new JoinGameRequest(0, "red"));
		testProxy.login(new Credentials("Brooke", "brooke"));
		testProxy.join(new JoinGameRequest(0, "orange"));
		testProxy.login(new Credentials("Pete", "pete"));
		testProxy.join(new JoinGameRequest(0, "blue"));
		testProxy.login(new Credentials("Mark", "mark"));
		testProxy.join(new JoinGameRequest(0, "green"));
	}

}
