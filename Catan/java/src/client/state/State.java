package client.state;

import client.base.IController;
import shared.model.facade.ModelFacade;

public abstract class State{
	ModelFacade modelfacade;
	
	final void setModelFacade(ModelFacade modelfacade){
		this.modelfacade = modelfacade;
	}
	
	final ModelFacade getModelFacade(){
		return modelfacade;
	}
	
	public abstract void setState(IController controller, State state);

}
