package server.handlers;

import java.io.IOException;

import server.handlers.factories.GameCommandFactory;
import client.base.IAction;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GameHandler implements HttpHandler{

	@Override
	public void handle(HttpExchange arg0) throws IOException {
		IAction event = GameCommandFactory.create(arg0);
		event.execute();
	}

}
