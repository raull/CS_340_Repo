package server.persistence;

import server.facade.ServerFacade;
import server.persistence.provider.Provider;

public class PersistenceStore {
	private Provider provider;
	private int commandTicks;
	
	ServerFacade serverFacade = ServerFacade.instance();
	
	/**
	 * constructor for persistence store
	 * load is called only in constructor
	 */
	public PersistenceStore() {
		serverFacade.load();
	}
	
	/**
	 * call persist
	 */
	public void persist() {
		serverFacade.persist(this.provider);
	}
	
	/**
	 * set the provider of the persistence store
	 * @param provider
	 */
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	
	/**
	 * increase the command ticks
	 */
	public void increaseTick() {
		this.commandTicks++;
	}
	
	/**
	 * get how many ticks the server is now at
	 * @return
	 */
	public int getTicks() {
		return this.commandTicks;
	}
}
