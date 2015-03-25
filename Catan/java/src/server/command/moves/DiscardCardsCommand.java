package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.moves.DiscardCards;
import shared.proxy.moves.ResourceList;

/**
 * Calls discard cards on server facade
 * @author thyer
 *
 */
public class DiscardCardsCommand extends ServerCommand {

	public DiscardCardsCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		
		DiscardCards discardCards = gson.fromJson(json, DiscardCards.class);
		ResourceList list = discardCards.getDiscardedCards();
						
		ServerFacade.instance().addCommand(json, gameId);
		return ServerFacade.instance().discardCards(gameId, discardCards.getPlayerIndex(), list.getResourceDeck());
	}

}
