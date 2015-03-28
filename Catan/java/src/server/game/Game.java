package server.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;

import shared.locations.VertexLocation;
import shared.model.board.Edge;
import shared.model.facade.ModelFacade;
import shared.model.game.TurnManager;
import shared.model.game.User;
import shared.proxy.games.Player;

/**
 * A class to represent a game on the server.
 * @author raulvillalpando
 *
 */
public class Game {
	
	/**
	 * THe ID of the game
	 */
	@Expose
	private int id; //the game's id
	/**
	 * THe name of the game
	 */
	@Expose
	private String title; //game's name
	
	/**
	 * The user with longest road
	 */
	private int longestRoadIndex;
	
	/**
	 * The user with largest army
	 */
	private int largestArmyIndex;
	
	/**
	 * The Model Facade of the game's model to perform all related operations.
	 */
	private ModelFacade modelFacade;
	
	@Expose
	private Player[] players;
	
	public Game(int id, String name, ModelFacade modelFacade) {
		super();
		this.id = id;
		this.title = name;
		this.modelFacade = modelFacade;
		this.longestRoadIndex = -1;
		this.largestArmyIndex = -1;
		this.players = new Player[4];
	}

	/**
	 * Returns the unique id of the game.
	 * @return The game id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the unique id of the game.
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the name of the game.
	 * @return the name of the game.
	 */
	public String getName() {
		return title;
	}

	/**
	 * Sets the name of the game.
	 * @param name
	 */
	public void setName(String name) {
		this.title = name;
	}

	/**
	 * Returns the game's modelFacade object.
	 * @return The game's modelFacade.
	 */
	public ModelFacade getModelFacade() {
		return modelFacade;
	}

	/**
	 * Sets the game's modelFacade object.
	 * @param modelFacade
	 */
	public void setModelFacade(ModelFacade modelFacade) {
		this.modelFacade = modelFacade;
	}
		
	/**
	 * get player index with longest road
	 * @return
	 */
	public int getLongestRoadPlayer() {
		return longestRoadIndex; 
	}
	
	/**
	 * calculates which user has longest road, or no user
	 * currently going for most roads
	 * @return
	 */
	public void calcLongestRoadPlayer(){
		List<User> users = modelFacade.turnManager().getUsers();
		User longestRoadUser = null;
		int allRoads = 15; // all users start off with 15 roads
		int roadCount = 0; //keep track of most roads count
		if(this.longestRoadIndex != -1) {
			longestRoadUser = modelFacade.turnManager().getUserFromIndex(longestRoadIndex);
			roadCount = allRoads - longestRoadUser.getUnusedRoads();
		}
		
		
		for(User user : users) {
			int currUserRoads = allRoads - user.getUnusedRoads();
			if( currUserRoads >= 5 && currUserRoads > roadCount) {
				longestRoadUser = user;
				roadCount = currUserRoads;
				longestRoadIndex = user.getTurnIndex();
			}
		}
	}
	
	/**
	 * Returns an integer indicating the index of the player with the longest continuous road
	 * @return the index of the player if there is one, otherwise -1
	 */
	public int getLongestRoadIndex(){
		List<User> users = modelFacade.turnManager().getUsers();
		int index = -1;
		int longestRoad = 0;
		
		//finds longest road of each player
		for(User user: users){
			if(user.getOccupiedEdges().size()<5){
				continue; //don't even bother unless the player has 5+ roads
			}
			//checks for longest road beginning at each road
			try{
				for(Edge e : user.getOccupiedEdges()){
					List<Edge> edges = excludeEdge(user.getOccupiedEdges(),e);
					//This is the tricky part - checks for adjacency to a vertex of a given road, but removes that road before checking
					VertexLocation[] vertices = e.getLocation().getAdjacentVertices();
					int temp = Math.max(rGetLongestRoad(vertices[0], edges), rGetLongestRoad(vertices[1], edges));
					if(temp > longestRoad){
						longestRoad = temp;
						index = user.getPlayerInfo().getPlayerIndex();
					}
					else if (temp == longestRoad && user.getPlayerInfo().getPlayerIndex()==longestRoadIndex){
						index = user.getPlayerInfo().getPlayerIndex(); //breaks ties based on who got there first
					}
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		if(longestRoad < 5)
			return -1;
		else
			return index;
	}
	
	/**
	 * Recursive method that spans all possible permutations of the user's roads in combination
	 * @param v the starting vertex that a road must be connected to
	 * @param edges the available roads left to be connected in this continguous segment
	 * @return the number of roads branching off of this vertex + 1 (accounts for the road removed that provided the initial vertex)
	 */
	private int rGetLongestRoad(VertexLocation v, List<Edge> edges) {
		if(edges.size()==0){
			return 1; 			//base case, accounts for road just removed
		}
		List<Integer> permutations = new ArrayList<Integer>();
		for(Edge edge : edges){
			//this checks whether the given edge is connected to our vertex of interest, if so, uses that edge as the next recursive call
			if(edge.getLocation().getAdjacentVertices()[0].equals(v)){
				permutations.add(1 + rGetLongestRoad(edge.getLocation().getAdjacentVertices()[1], this.excludeEdge(edges, edge)));
			}
			else if(edge.getLocation().getAdjacentVertices()[1].equals(v)){
				permutations.add(1 + rGetLongestRoad(edge.getLocation().getAdjacentVertices()[0], this.excludeEdge(edges, edge)));
			}
			else{
				permutations.add(0); //avoids null pointer exception
			}
		}
		
		//maximizes the output of the recursive calls (one for each remaining edge in the for loop)
		int output = 0;
		for(int i : permutations){
			if(i>output)
				output = 0;
		}
		return output;
	}

	/**
	 * Given a list of edges and an edge, returns a new list that does not contain the given edge
	 * @param l the list of elements
	 * @param e the edge to be excluded
	 * @return a new list containing all elements of l that were not equal to e
	 */
	public List<Edge> excludeEdge(List<Edge> l, Edge e){
		ArrayList<Edge> list = new ArrayList<Edge>();
		for(Edge edge : l){
			if(!edge.getLocation().equals(e.getLocation())){
				list.add(edge);
			}
		}
		return list;
	}
	
	/**
	 * get player index with largest army
	 * @return
	 */
	public int getLargestArmyPlayer() {
		return this.largestArmyIndex;
	}
	
	/**
	 * calculates which user has largest army, or no user
	 * @return
	 */
	public void calcLargestArmyPlayer() {
		
		List<User> users = modelFacade.turnManager().getUsers();
		User largestArmyUser = null;
		int armyCount = 0; //keep track of largest army count
		if(largestArmyIndex != -1) {
			largestArmyUser = modelFacade.turnManager().getUserFromIndex(largestArmyIndex);
			armyCount = largestArmyUser.getSoldiers();
		}
		
		
		for(User user : users) {
			if(user.getSoldiers() >= 3 && user.getSoldiers() > armyCount) {
				largestArmyUser = user;
				armyCount = user.getSoldiers();
				largestArmyIndex = user.getTurnIndex();
			}
		}
		
	}
	
	public Player[] getPlayers() {
		
		TurnManager tm = modelFacade.turnManager();
		ArrayList<User> users = new ArrayList<>(tm.getUsers());
		ArrayList<Player> players = new ArrayList<>();
		for (User user : users) {
			Player newPlayer = new Player(user.getName(), user.getCatanColor().toString().toLowerCase(), user.getPlayerID());
			players.add(newPlayer);
		}
		
		return players.toArray(new Player[players.size()]);
	}
	
	public JsonElement jsonRepresentation() {
		JsonObject gameJSON = new JsonObject();
		gameJSON.addProperty("id", id);
		gameJSON.addProperty("title", title);
		
		ArrayList<Player> playersArray = new ArrayList<>(Arrays.asList(getPlayers()));
		JsonArray playersJSON = new JsonArray();
		Gson gson = new Gson();
		
		for (int i = 0; i < 4; i++) {
			if (i >= playersArray.size()) {
				playersJSON.add(new JsonObject());
			} else {
				Player player = playersArray.get(i);
				String json = gson.toJson(player);
				JsonParser parser = new JsonParser();
				playersJSON.add(parser.parse(json));
			}
		}
		
		gameJSON.add("players", playersJSON);
		
		
		return gameJSON;
	}
	
}
