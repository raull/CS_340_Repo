package client.manager;


import client.data.GameInfo;
import client.data.PlayerInfo;
import client.poller.Poller;
import shared.definitions.CatanColor;
import shared.model.facade.ModelFacade;
import shared.model.game.TurnPhase;
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
	private ServerProxy serverProxy = new ServerProxy();
	private ModelFacade modelFacade = new ModelFacade();
	private PlayerInfo currentPlayerInfo = new PlayerInfo();
	private GameInfo currentGameInfo = new GameInfo();
	private Poller serverPoller = new Poller(serverProxy, modelFacade);
	
	private ClientManager() {
	}
	
	public static ClientManager instance(){
		if (instance != null) {
			return instance;
		} else {
			instance = new ClientManager();
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
	
	public void startServerPoller() {
		serverPoller.run();
	}
	

}
