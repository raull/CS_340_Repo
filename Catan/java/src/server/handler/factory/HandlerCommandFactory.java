package server.handler.factory;

import server.command.MockCommand;
import server.command.ServerCommand;
import server.command.game.GameCreateCommand;
import server.command.game.GameJoinCommand;
import server.command.game.GameListCommand;
import server.command.game.GameLoadCommand;
import server.command.game.GameModelCommand;
import server.command.game.GameResetCommand;
import server.command.game.GameSaveCommand;
import server.command.moves.BuildCityCommand;
import server.command.moves.BuildRoadCommand;
import server.command.moves.BuildSettlementCommand;
import server.command.moves.BuyDevCardCommand;
import server.command.moves.DiscardCardsCommand;
import server.command.moves.FinishTurnCommand;
import server.command.moves.MaritimeTradeCommand;
import server.command.moves.OfferTradeCommand;
import server.command.moves.PlayMonopolyCardCommand;
import server.command.moves.PlayMonumentCardCommand;
import server.command.moves.PlayRBCardCommand;
import server.command.moves.PlaySoldierCardCommand;
import server.command.moves.PlayYOPCardCommand;
import server.command.moves.RollNumberCommand;
import server.command.moves.SendChatCommand;
import server.command.user.LoginCommand;
import server.command.user.RegisterCommand;

import com.sun.net.httpserver.HttpExchange;

/**
 * A factory that delivers command objects for the GameHandler. 
 * @author thyer
 *
 */
public class HandlerCommandFactory implements CommandFactory{

	/**
	 * Creates and returns an IAction based on the instructions found within the HttpExchange object
	 * @param arg0 the HttpExchange object
	 * @return an IAction containing the appropriate behavior to be executed when appropriate
	 */
	@Override
	public ServerCommand create(HttpExchange arg0) {
		// parse out the HttpExchange object
		// create appropriate command object
		
		String uri = arg0.getRequestURI().toString();
		String[] arguments = uri.split("/");
		String request = arguments[arguments.length-1];
		//System.out.println("Request: " + request);
		//System.out.println("Query: " + arg0.getRequestURI().getQuery());
		
		//String[] requestSplit = request.split("o");
		//System.out.println("Test: " + requestSplit[0]);
		
		if (request.startsWith("model"))
		{
			request = "model";
		}
		
		switch (request) {
		case "login":
			return new LoginCommand(arg0);
		case "register":
			return new RegisterCommand(arg0);
		case "list":
			return new GameListCommand(arg0);
		case "create":
			return new GameCreateCommand(arg0);
		case "join":
			return new GameJoinCommand(arg0);
		case "save":
			return new GameSaveCommand(arg0);
		case "load":
			return new GameLoadCommand(arg0);
		case "model":
			return new GameModelCommand(arg0);
		case "reset":
			return new GameResetCommand(arg0);
		case "sendChat":
			return new SendChatCommand(arg0);
		case "rollNumber":
			return new RollNumberCommand(arg0);
		case "finishTurn":
			return new FinishTurnCommand(arg0);
		case "buyDevCard":
			return new BuyDevCardCommand(arg0);
		case "Year_of_Plenty":
			return new PlayYOPCardCommand(arg0);
		case "Road_Building":
			return new PlayRBCardCommand(arg0);
		case "Soldier":
			return new PlaySoldierCardCommand(arg0);
		case "Monopoly":
			return new PlayMonopolyCardCommand(arg0);
		case "Monument":
			return new PlayMonumentCardCommand(arg0);
		case "buildRoad":
			return new BuildRoadCommand(arg0);
		case "buildSettlement":
			return new BuildSettlementCommand(arg0);
		case "buildCity":
			return new BuildCityCommand(arg0);
		case "offerTrade":
			return new OfferTradeCommand(arg0);
		case "maritimeTrade":
			return new MaritimeTradeCommand(arg0);
		case "discardCards":
			return new DiscardCardsCommand(arg0);
		case "addAI":
			return new MockCommand(arg0);
		default:
			break;
		}
		
		return null;
	}

}
