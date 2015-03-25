package server.command.moves;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;
import shared.proxy.moves.SendChat;

/**
 * Calls send chat on server facade
 * @author thyer
 *
 */
public class SendChatCommand extends ServerCommand {

	public SendChatCommand(HttpExchange arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		SendChat sendChat = gson.fromJson(json, SendChat.class);
		
		ServerFacade.instance().addCommand(json, gameId);
		return ServerFacade.instance().sendChat(gameId, sendChat.getPlayerIndex(), sendChat.getContent());		
	}

}
