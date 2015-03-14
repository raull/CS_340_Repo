package server.handlers.factories;

import server.commands.ServerCommand;
import client.base.IAction;

import com.sun.net.httpserver.HttpExchange;

public class MockCommandFactory implements CommandFactory {

	@Override
	public ServerCommand create(HttpExchange arg0) {
		return new ServerCommand(arg0);
	}

}
