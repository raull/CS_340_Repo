package server.persistence;

import server.facade.ServerFacade;
import server.persistence.provider.Provider;

public class PersistenceStore {
	private Provider provider;
	private int commandTicks;
	
	ServerFacade serverFacade = ServerFacade.instance();
	
	/**
	 * Constructor for persistence store
	 * load is called only once in constructor
	 */
	public PersistenceStore() {
		serverFacade.load(this.provider);
	}
	
	/**
	 * Call persist and calls the serverFacade to persist all of the games and users
	 */
	public void persist() {
		serverFacade.persist(this.provider);
	}
	
	/**
	 * Set the provider of the persistence store
	 * @param provider
	 */
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	
	/**
	 * Increase the command ticks for persistence frequency
	 */
	public void increaseTick() {
		this.commandTicks++;
	}
	
	/**
	 * Get how many ticks the server is now at
	 * @return
	 */
	public int getTicks() {
		return this.commandTicks;
	}
}
