package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.moves.Year_of_Plenty_;

/**
 * Calls play YOP card on server facade
 * @author rental
 *
 */
public class PlayYOPCardCommand extends ServerCommand {

	public PlayYOPCardCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		Year_of_Plenty_ yearOfPlenty = gson.fromJson(json, Year_of_Plenty_.class);
		return ServerFacade.instance().playYearOfPlenty(gameId, yearOfPlenty.getPlayerIndex(), yearOfPlenty.getResource1(), yearOfPlenty.getResource2());
	}

}
