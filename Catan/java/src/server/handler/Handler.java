package server.handler;

import java.io.IOException;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.handler.factory.CommandFactory;
import server.handler.factory.HandlerCommandFactory;
import server.handler.factory.MockCommandFactory;
import client.base.IAction;

import com.google.gson.JsonElement;
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
		try{
			JsonElement response = event.execute();
		} catch(ServerInvalidRequestException e1){
			//do stuff
		}
	}

}
