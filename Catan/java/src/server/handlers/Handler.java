package server.handlers;

import java.io.IOException;

import server.command.ServerCommand;
import server.handlers.factories.CommandFactory;
import server.handlers.factories.HandlerCommandFactory;
import server.handlers.factories.MockCommandFactory;
import client.base.IAction;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Handler implements HttpHandler{
	
	CommandFactory factory;
	
	public Handler(boolean testing){
		if(testing)
			factory = new MockCommandFactory();
		else
			factory = new HandlerCommandFactory();
	}

	@Override
	public void handle(HttpExchange arg0) throws IOException {
		ServerCommand event = factory.create(arg0);
		event.execute();
	}

}
