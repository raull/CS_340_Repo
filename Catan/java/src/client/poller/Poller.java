package client.poller;

import com.google.gson.JsonObject;

import shared.model.model_facade.ModelFacade;
import shared.proxy.Proxy;

public class Poller {
	
	private Proxy proxy;
	public ModelFacade modelFacade; //has a pointer to model facade in order to update the model
	
	/**
	 * constructor of poller
	 * @param proxy pass in proxy or moxy
	 */
	public Poller(Proxy proxy) {
		this.proxy = proxy;
	}
	
	/**
	 * Polls server for a JSON response of current model state
	 */
	public JsonObject pollServer() {
		return null;
	}
	
	/**
	 * updates the model with the JSON response through the model facade
	 * @param the JSONresponse from the server
	 */
	public void updateClientModel(JsonObject jsonReponse) {
		
	}
	
}
