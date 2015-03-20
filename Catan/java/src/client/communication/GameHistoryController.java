package client.communication;

import java.util.*;

import client.base.*;
import client.manager.ClientManager;
import shared.definitions.*;
import shared.model.Model;
import shared.model.game.MessageLine;
import shared.model.game.MessageList;
import shared.model.game.User;


/**
 * Game history controller implementation
 */
public class GameHistoryController extends Controller implements IGameHistoryController, Observer {

	public GameHistoryController(IGameHistoryView view) {

		super(view);
		ClientManager.instance().getModelFacade().addObserver(this);
		initFromModel();
	}

	@Override
	public IGameHistoryView getView() {

		return (IGameHistoryView)super.getView();
	}

	private void initFromModel() 
	{
		//update();
	}

	@Override
	public void update(Observable o, Object arg) 
	{
		//retrieve LogEntries from model		
		Model model = ClientManager.instance().getModelFacade().model;
		MessageList logList = model.getLog();
		ArrayList<MessageLine> logs = logList.getLines();

		List<LogEntry> entries = new ArrayList<LogEntry>();
		for (MessageLine line : logs)
		{
			String playerName = line.getSource();
			//get player's color
			User user = model.getTurnManager().getUserFromName(playerName);

			assert (user != null) : "the user was null for a particular history message";

			entries.add(new LogEntry(user.getCatanColor(), line.getMessage())); //possibly null pointer exceptions being thrown here?
		}

		getView().setEntries(entries);
	}

}

