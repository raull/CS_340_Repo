package server.commands;

import com.sun.net.httpserver.HttpExchange;

import client.base.IAction;

public interface ServerCommand extends IAction {
	public void load(HttpExchange arg0);

	@Override
	public void execute();

}
