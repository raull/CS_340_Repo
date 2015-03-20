package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private Logger logger = null;
	
	public Handler(boolean testing){
		if(testing)
			factory = new MockCommandFactory();
		else
			factory = new HandlerCommandFactory();
	}

	@Override
	public void handle(HttpExchange arg0) throws IOException {
		ServerCommand event = factory.create(arg0);
		this.logInfo("Request: " + arg0.getRequestURI().toString());
		try{
			JsonElement response = event.execute();
			arg0.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getAsString().length());
			xmlStream.toXML(response, arg0.getResponseBody());
			arg0.getResponseBody().close();
			arg0.close();
		} catch(ServerInvalidRequestException e1){
			this.logError(e1.getMessage());
		}
	}
	
	public void setLogger(Logger l){
		logger = l;
	}
	
	private void logInfo(String s){
		if(logger!=null){
			logger.info(s);
		}
		else{
			System.out.println("WARNING: Logger never intialized");
		}
	}
	
	private void logError(String s){
		if(logger!=null){
			logger.log(Level.SEVERE, s);
		}
		else{
			System.out.println("WARNING: Logger never intialized");
		}
	}

}
