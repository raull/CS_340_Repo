package client.poller;

import com.google.gson.JsonObject;

import shared.proxy.Proxy;

public class Poller {
	
	private Proxy proxy;
	
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
	 * updates the client model with the JSON response
	 * @param the JSONresponse from the server
	 */
	public void updateClientModel(JsonObject jsonReponse) {
		
	}
	
}
