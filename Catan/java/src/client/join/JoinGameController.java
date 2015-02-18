package client.join;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import shared.definitions.CatanColor;
import shared.proxy.ProxyException;
import shared.proxy.ServerProxy;
import shared.proxy.games.JoinGameRequest;
import client.base.*;
import client.data.*;
import client.manager.ClientManager;
import client.misc.*;


/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController, Observer {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;
	
	private ServerProxy proxy = ClientManager.instance().getServerProxy();
	
	/**
	 * JoinGameController constructor
	 * 
	 * @param view Join game view
	 * @param newGameView New game view
	 * @param selectColorView Select color view
	 * @param messageView Message view (used to display error messages that occur while the user is joining a game)
	 */
	public JoinGameController(IJoinGameView view, INewGameView newGameView, 
								ISelectColorView selectColorView, IMessageView messageView) {

		super(view);

		setNewGameView(newGameView);
		setSelectColorView(selectColorView);
		setMessageView(messageView);
	}
	
	public IJoinGameView getJoinGameView() {
		
		return (IJoinGameView)super.getView();
	}
	
	/**
	 * Returns the action to be executed when the user joins a game
	 * 
	 * @return The action to be executed when the user joins a game
	 */
	public IAction getJoinAction() {
		
		return joinAction;
	}

	/**
	 * Sets the action to be executed when the user joins a game
	 * 
	 * @param value The action to be executed when the user joins a game
	 */
	public void setJoinAction(IAction value) {	
		
		joinAction = value;
	}
	
	public INewGameView getNewGameView() {
		
		return newGameView;
	}

	public void setNewGameView(INewGameView newGameView) {
		
		this.newGameView = newGameView;
	}
	
	public ISelectColorView getSelectColorView() {
		
		return selectColorView;
	}
	public void setSelectColorView(ISelectColorView selectColorView) {
		
		this.selectColorView = selectColorView;
	}
	
	public IMessageView getMessageView() {
		
		return messageView;
	}
	public void setMessageView(IMessageView messageView) {
		
		this.messageView = messageView;
	}

	@Override
	public void start() {
		
		getJoinGameView().showModal();
	}

	@Override
	public void startCreateNewGame() {
		
		getNewGameView().showModal();
	}

	@Override
	public void cancelCreateNewGame() {
		
		getNewGameView().closeModal();
	}

	@Override
	public void createNewGame() {
		
		getNewGameView().closeModal();
	}

	@Override
	public void startJoinGame(GameInfo game) {

		getSelectColorView().showModal();
	}

	@Override
	public void cancelJoinGame() {
	
		getJoinGameView().closeModal();
	}

	@Override
	public void joinGame(CatanColor color) {
		
		// If join succeeded
		JoinGameRequest tempRequest = new JoinGameRequest(0, color.toString()); //TODO: Get the real gameID number
		try {
			proxy.join(tempRequest);
			getSelectColorView().closeModal();
			getJoinGameView().closeModal();
			joinAction.execute(); //brings up the waiting modal
		} catch (ProxyException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			JsonElement je = proxy.list();
			this.getJoinGameView().setGames(this.getGameInfo(je), null);//TODO fill in PlayerInfo
		} catch (ProxyException e) {
			e.printStackTrace();
		}
		
	}
	
	private GameInfo[] getGameInfo(JsonElement je){
		System.out.println("Entering getGameInfo method");
		ArrayList<GameInfo> gameInfoList = new ArrayList<GameInfo>();
		JsonArray gameArray = je.getAsJsonArray();
		for(int i=0; i < gameArray.size(); ++i){
			Gson gson = new Gson();
			JsonObject game = gameArray.get(i).getAsJsonObject();
			String title = gson.fromJson(game.get("title"), String.class);
			int id = game.get("id").getAsInt();
			GameInfo gi = new GameInfo();
			gi.setId(id);
			gi.setTitle(title);
			populatePlayerInfo(gi, game.get("players").getAsJsonArray());
			
			gameInfoList.add(gi);
		}
		return (GameInfo[]) gameInfoList.toArray();
	}

	private void populatePlayerInfo(GameInfo gi, JsonArray players) {
		System.out.println("Entering populatePlayerInfo method");
		for(int i = 0; i < players.size(); ++i){
			Gson gson = new Gson();
			JsonObject player = players.get(i).getAsJsonObject();
			CatanColor color = gson.fromJson(player.get("color"), CatanColor.class);
			String name = gson.fromJson(player.get("name"), String.class);
			int id = player.get("id").getAsInt();
			
			PlayerInfo pi = new PlayerInfo();
			pi.setColor(color);
			pi.setName(name);
			pi.setId(id);
			gi.addPlayer(pi);
		}
		
	}

}

