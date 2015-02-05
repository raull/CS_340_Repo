package client.poller;

import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.JsonObject;

import shared.model.Model;
import shared.model.facade.ModelFacade;
import shared.proxy.Proxy;
import shared.proxy.ProxyException;

public class Poller {
	
	private Proxy proxy;
	public ModelFacade modelFacade; //has a pointer to model facade in order to update the model
	
	//poll the server at regular intervals
	private Timer pollerTimer = new Timer(false); //not daemon thread 
	
	/**
	 * constructor of poller
	 * @param proxy pass in proxy or moxy
	 */
	public Poller(Proxy proxy) {
		this.proxy = proxy;
		pollerTimer.scheduleAtFixedRate( 
			new TimerTask() {
				public void run() { pollServer(); }
			}, 0, 3000 ); //poll the server every 3000ms, or 3s
	}
	
	/**
	 * Polls server for a possible updated version of model, which is sent to model facade to update
	 */
	public void pollServer() {
		Model currModel = modelFacade.getModel();
		JsonObject response = null;
		try {
			response = proxy.model(currModel.getVersion());
		} catch (ProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		modelFacade.updateModel(response);
	}
	
	/**
	 * updates the model with the JSON response through the model facade
	 * @param the JSONresponse from the server
	 */
	public void updateClientModel(JsonObject jsonReponse) {
		//don't really need anymore
	}
	
}