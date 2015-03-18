package server.command;

import server.exception.ServerInvalidRequestException;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

public class MockCommand extends ServerCommand{

	public MockCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

}
