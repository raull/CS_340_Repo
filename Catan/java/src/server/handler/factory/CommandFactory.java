package server.handler.factory;

import server.command.ServerCommand;
import client.base.IAction;

import com.sun.net.httpserver.HttpExchange;

public interface CommandFactory {

	ServerCommand create(HttpExchange arg0);
	
	

}
