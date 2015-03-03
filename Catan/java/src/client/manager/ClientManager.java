package client.manager;


import com.google.gson.JsonElement;

import client.data.GameInfo;
import client.data.PlayerInfo;
import client.misc.MessageView;
import client.poller.Poller;
import shared.definitions.CatanColor;
import shared.model.facade.ModelFacade;
import shared.model.game.TurnPhase;
import shared.proxy.ProxyException;
import shared.proxy.ServerProxy;


/**
 * Client Manager is a singleton class that handles the {@link ModelFacade} and {@link ServerProxy}
 * for easier access. This class alse serves to store values for easy access as well, for example the user ID that the client is currently logged in as
 * 
 * @author thyer
 *
 */
public class ClientManager {
	
	private static ClientManager instance;
	private ServerProxy serverProxy; 
	private ModelFacade modelFacade = new ModelFacade();
	private PlayerInfo currentPlayerInfo = new PlayerInfo();
	private GameInfo currentGameInfo = new GameInfo();
	private Poller serverPoller;
	private boolean gameStarted = false;
	private boolean isUserRolling = false;
	
	private boolean serverPollerRunning = false;
	
	private ClientManager() {
		serverProxy = new ServerProxy();
		serverPoller = new Poller(serverProxy, modelFacade);
	}
	
	private ClientManager(String host, String port)
	{
		serverProxy = new ServerProxy(host, port);
		serverPoller = new Poller(serverProxy, modelFacade);
	}
	
	public static ClientManager instance(){
		if (instance != null) {
			return instance;
		} else {
			instance = new ClientManager();
			return instance;
		}
	}
	
	public static ClientManager instance(String host, String port)
	{
		if (instance != null) {
			return instance;
		} else {
			instance = new ClientManager(host, port);
			return instance;
		}
	}
	
	/**
	 * Get the client Model Facade
	 * @return The singleton copy of the Model Facade
	 */
	public ModelFacade getModelFacade() {
		return modelFacade;
	}
	
	/**
	 * Get the client Server Proxy
	 * @return The singleton copy of the Server Proxy
	 */
	public ServerProxy getServerProxy() {
		return serverProxy;
	}
	
	public PlayerInfo getCurrentPlayerInfo(){
		return currentPlayerInfo;
	}
	
	
	public void setPlayerName(String name){
		currentPlayerInfo.setName(name);
	}
	
	public void setPlayerID(int id){
		currentPlayerInfo.setId(id);
	}
	
	public void setPlayerIndex(int index){
		currentPlayerInfo.setPlayerIndex(index);
	}
	
	public void setPlayerColor(CatanColor color){
		currentPlayerInfo.setColor(color);
	}

	public GameInfo getCurrentGameInfo() {
		return currentGameInfo;
	}

	public void setCurrentGameInfo(GameInfo gi) {
		currentGameInfo.setId(gi.getId());
		currentGameInfo.setTitle(gi.getTitle());
		for(PlayerInfo pi : gi.getPlayers()){
			currentGameInfo.addPlayer(pi);
		}
	}
	
	public TurnPhase getCurrentTurnPhase() {
		return modelFacade.turnManager().currentTurnPhase();
	}
	
	/**
	 * Runs the server poller. If it was already running it does nothing
	 */
	public void startServerPoller() {
		if (!serverPollerRunning) {
			serverPoller.run();
		}
	}
	
	public boolean isServerPollerRunning() {
		return serverPollerRunning;
	}
	
	public void startGame() {
		gameStarted = true;
	}
	
	public boolean hasGameStarted() {
		return gameStarted;
	}
	
	public void forceUpdate() {
		JsonElement model;
		try {
			model = ClientManager.instance().getServerProxy().model(-1);
			ClientManager.instance().getModelFacade().updateModel(model);

		} catch (ProxyException e) {
			MessageView alertView = new MessageView();
			alertView.setTitle("Error");
			alertView.setMessage("Network Error. Please check your connection.");
			alertView.showModal();
		}
	}

	public boolean isUserRolling() {
		return isUserRolling;
	}

	public void setUserRolling(boolean isUserRolling) {
		this.isUserRolling = isUserRolling;
	}
	
}
