package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.handler.factory.CommandFactory;
import server.handler.factory.HandlerCommandFactory;
import server.handler.factory.MockCommandFactory;
import client.base.IAction;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Handler implements HttpHandler{
	
	CommandFactory factory;
	private XStream xmlStream = new XStream(new DomDriver());
	
	public Handler(boolean testing){
		if(testing)
			factory = new MockCommandFactory();
		else
			factory = new HandlerCommandFactory();
	}

	@Override
	public void handle(HttpExchange arg0) throws IOException {
		System.out.println("Handler received request: " + arg0.getRequestURI().toString());
		ServerCommand event = factory.create(arg0);
		System.out.println("ServerCommand created");
		try{
			System.out.println("Preparing to execute request command");
			JsonElement response = event.execute();
			System.out.println("Command executed");
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getAsString().length());
			System.out.println("Success headers sent");
			xmlStream.toXML(response, arg0.getResponseBody());
			arg0.getResponseBody().close();
			arg0.close();
		} catch(ServerInvalidRequestException e1){
			e1.printStackTrace();
		}
	}

}
