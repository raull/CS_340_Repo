package server.commands;

import com.sun.net.httpserver.HttpExchange;

public abstract class ServerCommand{
	
	protected HttpExchange httpObj;
	
	public ServerCommand(HttpExchange arg0){
		httpObj = arg0;
	}

	public abstract void execute();

}
