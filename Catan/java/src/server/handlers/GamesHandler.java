package server.handlers;

import java.io.IOException;

import server.handlers.factories.GamesCommandFactory;
import client.base.IAction;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GamesHandler implements HttpHandler{

	@Override
	public void handle(HttpExchange arg0) throws IOException {
		IAction move = GamesCommandFactory.create(arg0);
		move.execute();
		
	}

}
