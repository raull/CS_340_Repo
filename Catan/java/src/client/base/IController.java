package client.base;

import client.state.State;

/**
 * Base controller interface
 */
public interface IController
{
	
	/**
	 * View getter
	 * 
	 * @return The controller's view
	 */
	IView getView();
}

