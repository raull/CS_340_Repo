package server;

import java.io.*;
import java.net.*;
import java.util.logging.*;

import server.handler.Handler;

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
	
	/**
	 * Initializes the server's logger object.
	 * @throws IOException
	 */
	private static void initLog() throws IOException {
		
		Level logLevel = Level.FINE;
		
		logger = Logger.getLogger("damocles"); 
		logger.setLevel(logLevel);
		logger.setUseParentHandlers(false);
		
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);

		FileHandler fileHandler = new FileHandler("logs/log.txt", false);
		fileHandler.setLevel(logLevel);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
	}

	
	private HttpServer server;
	private Handler serverHandler;
	
	private Server() {
		serverHandler = new Handler(false);
		return;
	}
	
	private Server(boolean testing)
	{
		serverHandler = new Handler(testing);
	}
	
	/**
	 * Starts the server on the specified port (8081 is default).
	 */
	private void run() {
			
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
		
		server.createContext("/docs/api/data", new Handlers.JSONAppender(""));
		server.createContext("/docs/api/view", new Handlers.BasicFile(""));
		server.createContext("/", serverHandler);
		serverHandler.setLogger(logger);
		
		logger.info("Starting HTTP Server");

		server.start();
	}

	

	
	public static void main(String[] args) {
		if(args.length>0){
			SERVER_PORT_NUMBER = Integer.parseInt(args[0]);
		}
		else{
			SERVER_PORT_NUMBER = 8081;
		}
		
		boolean testing = false;
		if (args.length == 2)
		{
			testing = Boolean.parseBoolean(args[1]);
		}
		new Server(testing).run();
		logger.info("Server running on port: " + SERVER_PORT_NUMBER);
	}

	
}