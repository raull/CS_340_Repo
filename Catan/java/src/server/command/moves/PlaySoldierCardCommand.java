package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.moves.Soldier_;

/**
 * Calls play soldier card on server facade
 * @author thyer
 *
 */
public class PlaySoldierCardCommand extends ServerCommand {

	public PlaySoldierCardCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		return execute(this.json);
	}

	@Override
	public JsonElement execute(String json)
			throws ServerInvalidRequestException {
		Soldier_ soldier = gson.fromJson(json, Soldier_.class);
		
		ServerFacade.instance().addCommand(json, gameId);
		return ServerFacade.instance().robPlayer(gameId, soldier.getPlayerIndex(),
				soldier.getVictimIndex(), soldier.getLocation(), true);
	}

}
