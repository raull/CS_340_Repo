package client.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import shared.model.Model;
import shared.model.game.MessageLine;
import shared.model.game.MessageList;
import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.moves.SendChat;
import client.base.*;
import client.data.PlayerInfo;
import client.manager.ClientManager;
import client.misc.MessageView;


/**
 * Chat controller implementation
 */
public class ChatController extends Controller implements IChatController, Observer {

	public ChatController(IChatView view) {
		
		super(view);
		ClientManager.instance().getModelFacade().addObserver(this);
	}

	@Override
	public IChatView getView() {
		return (IChatView)super.getView();
	}

	@Override
	public void sendMessage(String message) 
	{
		PlayerInfo client = ClientManager.instance().getCurrentPlayerInfo();
		SendChat chat = new SendChat(client.getPlayerIndex(), message);
		
		try {
			ClientManager.instance().getServerProxy().sendChat(chat);
			ClientManager.instance().forceUpdate();
		} catch (ProxyException e) {
			// TODO Auto-generated catch block
			MessageView errorMessage = new MessageView();
			errorMessage.setTitle("Error");
			errorMessage.setMessage("Something wrong happened while trying to send the chat. Please try again later.");
			errorMessage.showModal();
		}
	}

	@Override
	public void update(Observable o, Object arg) 
	{
		//retrieve LogEntries from model		
		Model model = ClientManager.instance().getModelFacade().model;
		MessageList chatList = model.getChat();
		ArrayList<MessageLine> chats = chatList.getLines();
		
		List<LogEntry> entries = new ArrayList<LogEntry>();
		for (MessageLine line : chats)
		{
			String playerName = line.getSource();
			//get player's color
			User user = model.turnManager.getUserFromName(playerName);
			
			entries.add(new LogEntry(user.getCatanColor(), line.getMessage()));
		}
		
		getView().setEntries(entries);
	}

}

