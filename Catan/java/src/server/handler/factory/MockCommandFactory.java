package server.handler.factory;

import server.command.MockCommand;
import server.command.ServerCommand;
import client.base.IAction;

import com.sun.net.httpserver.HttpExchange;

public class MockCommandFactory implements CommandFactory {

	@Override
	public ServerCommand create(HttpExchange arg0) {
		return new MockCommand(arg0);
	}

}
