package server.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.handler.factory.CommandFactory;
import server.handler.factory.HandlerCommandFactory;
import server.handler.factory.MockCommandFactory;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Handler implements HttpHandler{
	
	CommandFactory factory;
	private Logger logger = null;
	
	public Handler(boolean testing){
		if(testing)
			factory = new MockCommandFactory();
		else
			factory = new HandlerCommandFactory();
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerCommand event = factory.create(exchange);
		this.logInfo("Request: " + exchange.getRequestURI().toString());
		try{
			System.out.println(event);
			JsonElement response = event.execute();
			System.out.println(response.toString());
			//application/text for now, for "success" on login/register
//			if(response.toString().equals("\"Success\"")) {
				exchange.getResponseHeaders().add("Content-Type", "application/text");
//			}
//			else{
//				exchange.getResponseHeaders().add("Content-Type", "application/json");
//			}
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			//output stream throw in java string
			PrintWriter writer = new PrintWriter(exchange.getResponseBody());
			writer.println(response.getAsString());
			System.out.println("response: " + response.getAsString());
			writer.close();
			exchange.getResponseBody().close();
			exchange.close();
		} catch(ServerInvalidRequestException e1){
			this.logError(e1.getMessage());
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
			exchange.getResponseBody().close();
			exchange.close();
		}
//		catch(Exception anythingelse) {
//			anythingelse.printStackTrace();
//			System.out.println("some sort exception happened: " + anythingelse.getLocalizedMessage());
//		}
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
