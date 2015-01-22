package shared.proxy;

import java.util.List;

import shared.model.Model;
import shared.proxy.game.*;
import shared.proxy.games.*;
import shared.proxy.moves.*;
import shared.proxy.user.*;
import shared.proxy.util.*;

/**
 * Proxy used by the client to send commands to the server
 * @author Kent
 *
 */
public class ServerProxy implements Proxy{

	@Override
	public void login(Credentials cred) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(Credentials cred) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Game> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NewGame create(CreateGameRequest CreateRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void join(JoinGameRequest JoinRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(SaveGameRequest SaveRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(LoadGameRequest LoadRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Model model(int version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model reset() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model postCommands(List<String> commandslist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getCommands() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model sendChat(SendChat sendchat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model rollNumber(RollNumber rollnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model robPlayer(RobPlayer robplayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model finishTurn(FinishMove finishmove) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model buyDevCard(BuyDevCard buydev) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model Year_of_Plenty(Year_of_Plenty_ yop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model Road_Building(Road_Building_ roadbuild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model Soldier(Soldier_ soldier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model Monopoly(Monopoly_ monopoly) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model Monument(Monument_ monument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model buildRoad(BuildRoad buildroad) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model buildSettlement(BuildSettlement buildsettlement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model buildCity(BuildCity buildcity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model offerTrade(OfferTrade tradeOffer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model acceptTrade(AcceptTrade tradeAccept) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model maritimeTrade(MaritimeTrade tradeMaritime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model discardCards(DiscardCards discard) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeLogLevel(ChangeLogLevelRequest cllr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAI(AddAIRequest addAIreq) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> listAI() {
		// TODO Auto-generated method stub
		return null;
	}

}
