package server.command;

import server.exception.ServerInvalidRequestException;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

public class MockCommand extends ServerCommand{
	boolean sendCookie = false;

	public MockCommand(HttpExchange arg0) {
		super(arg0);
		if(arg0.getRequestHeaders() != null){
			
		}
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		if(sendCookie){
			//include cookie in JSON
		}
		// TODO Auto-generated method stub
		return null;
	}

}
