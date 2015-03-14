package server.handlers;

import java.io.IOException;

import server.handlers.factories.UserCommandFactory;
import client.base.IAction;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class UserHandler implements HttpHandler{
	public UserHandler(){

	}
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		IAction event = UserCommandFactory.create(arg0);
		event.execute();
		
	}

}
