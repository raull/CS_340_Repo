package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.model.cards.ResourceCardDeck;
import shared.proxy.moves.OfferTrade;
import shared.proxy.moves.ResourceList;

/**
 * calls offer trade on server facade
 * @author rental
 *
 */
public class OfferTradeCommand extends ServerCommand {

	public OfferTradeCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
	
		return execute(this.json);
	}

	@Override
	public JsonElement execute(String json) throws ServerInvalidRequestException {
		OfferTrade offertrade = gson.fromJson(json, OfferTrade.class);
		ResourceList rl = offertrade.getOffer();
		ResourceCardDeck senderDeck = rl.getResourceDeck();
		ResourceCardDeck receiverDeck = rl.getReceiverDeck();
		ServerFacade.instance().addCommand(json, gameId);
		return ServerFacade.instance().offerTrade(gameId, offertrade.getPlayerIndex(),
				offertrade.getReceiver(), senderDeck, receiverDeck);
	}

}
