package client.join;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import shared.definitions.CatanColor;
import shared.proxy.ProxyException;
import shared.proxy.ServerProxy;
import shared.proxy.games.CreateGameRequest;
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
	private Timer gameTimer = new Timer(false);
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
		
		ClientManager.instance().getModelFacade().addObserver(this);
		
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
		try {
			//if this is coming from end of some other game
			//set game started back to false and stop poller
			if(ClientManager.instance().hasGameStarted()) {
				//ClientManager.instance().setGameStarted(false);
				//ClientManager.instance().getPoller().stopPoller(true);
				ClientManager.instance().resetSelf();
				ClientManager.instance().getPoller().pausePoller();
			}
			JsonElement je = ClientManager.instance().getServerProxy().list();
			this.getJoinGameView().setGames(this.getGameInfo(je), ClientManager.instance().getCurrentPlayerInfo());
			if(!this.getJoinGameView().isModalShowing()){
				getJoinGameView().showModal();
			}	
			startTimer();
		} catch (ProxyException e) {
			getMessageView().setTitle("Error");
			getMessageView().setMessage("Something went wrong while fetching active games. " + e.getMessage());
			getMessageView().showModal();
		}
		
	}

	/**
	 * Forces the update of the game list, called only by our timer
	 */
	private void updateGames() {
		if(this.getNewGameView().isModalShowing()){
			this.getNewGameView().closeModal();
		}
		JsonElement je = null;
		try {
			je = ClientManager.instance().getServerProxy().list();
			
		} catch (ProxyException e) {
			getMessageView().setTitle("Error");
			getMessageView().setMessage("Something went wrong while fetching active games. " + e.getMessage());
			getMessageView().showModal();
		}
		GameInfo[] updatedInfo = this.getGameInfo(je);
		if(needsUpdate(updatedInfo)){
			this.getJoinGameView().closeModal();
			this.getJoinGameView().setGames(updatedInfo, ClientManager.instance().getCurrentPlayerInfo());
			this.getJoinGameView().showModal();
		}
			
	}

	/**
	 * Determines whether the updatedInfo is equivalent to existing game information in the view
	 * @param updatedInfo the information to be potentially updated
	 * @return true if the information should be updated, otherwise false
	 */
	private boolean needsUpdate(GameInfo[] updatedInfo) {
		GameInfo [] oldInfo = this.getJoinGameView().getGames();
		if(oldInfo.length!=updatedInfo.length){ //new game
			return true;
		}
		else{
			for(int i = 0; i < updatedInfo.length; ++i){
				GameInfo current = updatedInfo[i];
				GameInfo old = oldInfo[i];
				if(current.getId()!=old.getId()){ //game ID changed (deletion and addition)
					return true;
				}
				List<PlayerInfo> oldPlayerInfo = old.getPlayers();
				List<PlayerInfo> currentPlayerInfo = current.getPlayers();
				if(oldPlayerInfo.size()!=currentPlayerInfo.size()){ //new player added
					return true;
				}
				for(int j = 0; j< currentPlayerInfo.size(); ++j){
					if(!oldPlayerInfo.get(j).equals(currentPlayerInfo.get(j))){ //player info updated
						return true;
					}
					else if(!oldPlayerInfo.get(j).getColor().equals(currentPlayerInfo.get(j).getColor())){ //color change
						return true;
					}
				}
				
			}
			return false; //no update needed
		}
	}

	@Override
	public void startCreateNewGame() {
		killTimer();
		getNewGameView().showModal();
	}

	/**
	 * Allows us to cancel a timer without getting an exception next time we want to schedule an identical task
	 */
	private void killTimer() {
		gameTimer.cancel();
		gameTimer = new Timer(false);
		
	}

	@Override
	public void cancelCreateNewGame() {
		this.getNewGameView().closeModal();
		startTimer();
	}

	@Override
	public void createNewGame() {
		//gets new game's info from the view
		String title = getNewGameView().getTitle();
		if(title.equals("")){
			getMessageView().setTitle("Error");
			getMessageView().setMessage("You cannot create a game with a blank title");
			getMessageView().showModal();
			return;
		}
		boolean randomHexes = getNewGameView().getRandomlyPlaceHexes();
		boolean randomNumbers = getNewGameView().getRandomlyPlaceNumbers();
		boolean randomPorts = getNewGameView().getUseRandomPorts();
		
		
		
		//sets up the request and transmits it through the proxy
		CreateGameRequest cr = new CreateGameRequest(randomHexes, randomNumbers, randomPorts, title);
		
		try {
			JsonElement je = ClientManager.instance().getServerProxy().list();
			GameInfo[] gameInfo = getGameInfo(je);
			int gameID = gameInfo.length;
			ClientManager.instance().getServerProxy().create(cr);
			JoinGameRequest jg = new JoinGameRequest(gameID, "red");
			proxy.join(jg);
			getNewGameView().closeModal();
			this.start();
		} catch (ProxyException e) {
			getMessageView().setTitle("Error");
			getMessageView().setMessage("New game could not be created. " + e.getMessage());
			getMessageView().showModal();
		}

	}

	@Override
	public void startJoinGame(GameInfo game) {
		killTimer();
		ClientManager.instance().setCurrentGameInfo(game); //sets the currentGameInfo, which we use to save the gameID
		getSelectColorView().enableAllButtons(); //resets all the buttons
		getSelectColorView().showModal();
		for(PlayerInfo pi : game.getPlayers()){
			if(pi.getId()!=ClientManager.instance().getCurrentPlayerInfo().getId()){	
				getSelectColorView().setColorEnabled(pi.getColor(), false); //disables colors already taken
			}
		}
	}

	@Override
	public void cancelJoinGame() {
		startTimer();
		getJoinGameView().closeModal();
		//ClientManager.instance().setCurrentGameInfo(null);
	}

	private void startTimer() {
		gameTimer.scheduleAtFixedRate( new TimerTask() {
			@Override
			public void run() {
				updateGames();
			}
		} , 0, 5000);
		
	}

	@Override
	public void joinGame(CatanColor color) {
		try {
			if(!this.canChooseColor(color)){
				getSelectColorView().setColorEnabled(color, false);
				getMessageView().setTitle("Color already chosen");
				getMessageView().setMessage("That color was just barely chosen by somebody else, please try another one");
				getMessageView().showModal();
				return;
			}
			int gameId = ClientManager.instance().getCurrentGameInfo().getId();
			JoinGameRequest tempRequest = new JoinGameRequest(gameId, color.toString().toLowerCase());
		
			proxy.join(tempRequest);
			// If join succeeded
			getSelectColorView().closeModal();
			getJoinGameView().closeModal();
			updateCurrentPlayerInfo();
			ClientManager.instance().startServerPoller();

			joinAction.execute(); //brings up the waiting modal
		} catch (ProxyException e) {
			getMessageView().setTitle("Error");
			getMessageView().setMessage("Error joining game: " + e.getMessage());
			getMessageView().showModal();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			JsonElement je = proxy.list();
			this.getJoinGameView().setGames(this.getGameInfo(je), ClientManager.instance().getCurrentPlayerInfo());
		} catch (ProxyException e) {
			e.printStackTrace();
		}
		
	}
	
	public void updateCurrentPlayerInfo(){
		try {
			Gson gson = new Gson();
			JsonObject jo = proxy.model(-1).getAsJsonObject(); //forces the server to give us a model
			JsonArray users = jo.get("players").getAsJsonArray(); //grabs all players in the game model
			for(int i=0; i<users.size(); ++i){
				if(users.get(i)==null){
					continue; //helps us ignore null players
				}
				else{
					JsonObject user = users.get(i).getAsJsonObject();
					String name = user.get("name").getAsString(); //we'll check if it's our player through the name
					
					if(name.equals(ClientManager.instance().getCurrentPlayerInfo().getName())){ //if it's our player
						CatanColor color = gson.fromJson(user.get("color"), CatanColor.class); //get the color
						int playerIndex = user.get("playerIndex").getAsInt(); //get the playerIndex
						
						ClientManager.instance().getCurrentPlayerInfo().setColor(color); //set both in ClientManager
						ClientManager.instance().getCurrentPlayerInfo().setPlayerIndex(playerIndex);
						break; //optimizes performance
					}
				}
			}
		} catch (ProxyException e) {
			getMessageView().setTitle("Error");
			getMessageView().setMessage("Failure updating currentPlayer. " + e.getMessage());
			getMessageView().showModal();
		}
	}
	
	private GameInfo[] getGameInfo(JsonElement je){
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
		GameInfo[] output = new GameInfo[gameInfoList.size()];
		for(int i=0; i<gameInfoList.size(); ++i){
			output[i] = gameInfoList.get(i);
		}
		return output;
	}

	private void populatePlayerInfo(GameInfo gi, JsonArray players) {
		for(int i = 0; i < players.size(); ++i){
			Gson gson = new Gson();
			JsonObject player = players.get(i).getAsJsonObject();
			CatanColor color = gson.fromJson(player.get("color"), CatanColor.class);
			String name = gson.fromJson(player.get("name"), String.class);
			JsonElement idElement = player.get("id");
			if(idElement==null){
				continue;
			}
			int id = idElement.getAsInt();
			
			PlayerInfo pi = new PlayerInfo();
			pi.setColor(color);
			pi.setName(name);
			pi.setId(id);
			gi.addPlayer(pi);
		}
		
	}
	
	/**
	 * This method quickly queries the server to find out if the color has already been chosen
	 * @param color The color desired by the user
	 * @return true if the color is still available, false otherwise
	 */
	private boolean canChooseColor(CatanColor color){
		try {
			JsonElement je = proxy.list();
			GameInfo[] updatedGames = this.getGameInfo(je); //gets all the games
			for(GameInfo gi : updatedGames){
				if(gi.getId()==ClientManager.instance().getCurrentGameInfo().getId()){ //finds our current game
					for(PlayerInfo pi : gi.getPlayers()){ //queries all players in our current game
						if(pi.getColor().equals(color)){  //if one of those players has our same color, fail immediately
							if(!pi.getName().equals(ClientManager.instance().getCurrentPlayerInfo().getName())){
								return false;
							}
						}
					}
				}
			}
		} catch (ProxyException e) { //probably won't happen unless the server goes down
			getMessageView().setTitle("Error");
			getMessageView().setMessage("Failure joining game, please check your connection (Error: " + e.getMessage() + ")");
			getMessageView().showModal();
		}
		//only gets here if the connection succeeded and nobody in our current game already has the current color
		return true;
	}

}

