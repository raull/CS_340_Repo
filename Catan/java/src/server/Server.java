package server;

import java.io.*;
import java.net.*;
import java.rmi.ServerException;
import java.util.logging.*;

import com.sun.net.httpserver.*;

public class Server {
	
	private static int SERVER_PORT_NUMBER;
	private static final int MAX_WAITING_CONNECTIONS = 10;
	
	private static Logger logger;
	
	static {
		try {
			initLog();
		}
		catch (IOException e) {
			System.out.println("Could not initialize log: " + e.getMessage());
		}
	}
	
	private static void initLog() throws IOException {
		
		Level logLevel = Level.FINE;
		
		logger = Logger.getLogger("damocles"); 
		logger.setLevel(logLevel);
		logger.setUseParentHandlers(false);
		
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);

		FileHandler fileHandler = new FileHandler("log.txt", false);
		fileHandler.setLevel(logLevel);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
	}

	
	private HttpServer server;
	
	private Server() {
		return;
	}
	
	private void run() {
		
		logger.info("Initializing Model");
		
		try {
			//ServerFacade.initialize();	
			if(1>0){
				throw new ServerException("new exception");
			}
		}
		catch (ServerException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
		
		logger.info("Initializing HTTP Server");
		
		try {
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER),
											MAX_WAITING_CONNECTIONS);
		} 
		catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);			
			return;
		}

		server.setExecutor(null); // use the default executor
		
		//server.createContext("/validateUser", validateUserHandler);
		
		logger.info("Starting HTTP Server");

		server.start();
	}

	//private ValidateUserHandler validateUserHandler = new ValidateUserHandler();

	
	public static void main(String[] args) {
		if(args.length>0){
			SERVER_PORT_NUMBER = Integer.parseInt(args[0]);
		}
		else{
			SERVER_PORT_NUMBER = 8081;
		}
		new Server().run();
		logger.info("Server running on port: " + SERVER_PORT_NUMBER);
	}

}