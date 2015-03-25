package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.moves.RobPlayer;

/**
 * Calls rob player command on server facade
 * @author thyer
 *
 */
public class RobPlayerCommand extends ServerCommand {

	public RobPlayerCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		return execute(this.json);
	}

	@Override
	public JsonElement execute(String json)
			throws ServerInvalidRequestException {
		RobPlayer robPlayer = gson.fromJson(json, RobPlayer.class);
		
		ServerFacade.instance().addCommand(json, gameId);
		return ServerFacade.instance().robPlayer(gameId, robPlayer.getPlayerIndex(),
				robPlayer.getVictimIndex(), robPlayer.getLocation(), false);
	}

}
