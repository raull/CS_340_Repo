package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.moves.Monopoly_;

/**
 * calls play monopolycard on server facade
 * @author thyer
 *
 */
public class PlayMonopolyCardCommand extends ServerCommand {

	public PlayMonopolyCardCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		return execute(this.json);
	}

	@Override
	public JsonElement execute(String json)
			throws ServerInvalidRequestException {
		Monopoly_ monopoly = gson.fromJson(json, Monopoly_.class); 
		
		ServerFacade.instance().addCommand(json, gameId);
		return ServerFacade.instance().playMonopoly(gameId, monopoly.getPlayerIndex(), monopoly.getResource());
	}

}
