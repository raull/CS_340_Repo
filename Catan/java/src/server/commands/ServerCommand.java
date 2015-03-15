package server.commands;

import com.sun.net.httpserver.HttpExchange;

import client.base.IAction;

public class ServerCommand{
	HttpExchange httpObj;
	public ServerCommand(HttpExchange arg0){
		httpObj = arg0;
	}

	public void execute(){
		
	}

}
