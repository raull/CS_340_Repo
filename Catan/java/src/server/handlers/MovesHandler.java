package server.handlers;

import java.io.IOException;

import server.handlers.factories.MovesCommandFactory;
import client.base.IAction;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MovesHandler implements HttpHandler{

	@Override
	public void handle(HttpExchange arg0) throws IOException {
		IAction event = MovesCommandFactory.create(arg0);
		
	}

}
