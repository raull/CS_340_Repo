package server.command.game;

import server.command.ServerCommand;
import server.exception.ServerInvalidRequestException;
import server.facade.ServerFacade;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Calls model on ServerFacade
 * @author thyer
 *
 */
public class GameModelCommand extends ServerCommand {

	public GameModelCommand(HttpExchange arg0) {
		super(arg0);
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {	
		int version = identifyVersion();
		return ServerFacade.instance().getModel(version, gameId);
	}

	@Override
	public JsonElement execute(String json)
			throws ServerInvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int identifyVersion()
	{
		String url = httpObj.getRequestURI().toString();
		String[] split = url.split("/");
		String modelParam = split[split.length - 1];
		try
		{
			String[] modelSplit = modelParam.split("=");
			int version = Integer.parseInt(modelSplit[modelSplit.length - 1]);
			return version;
		}
		catch (NumberFormatException e)
		{
			return -1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}

}
