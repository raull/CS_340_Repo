package client.login;

import client.base.*;
import client.manager.ClientManager;
import client.misc.*;

import java.util.*;

import shared.proxy.user.Credentials;



/**
 * Implementation for the login controller
 */
public class LoginController extends Controller implements ILoginController, Observer {

	private IMessageView messageView;
	private IAction loginAction;
	
	/**
	 * LoginController constructor
	 * 
	 * @param view Login view
	 * @param messageView Message view (used to display error messages that occur during the login process)
	 */
	public LoginController(ILoginView view, IMessageView messageView) {

		super(view);
		
		this.messageView = messageView;
		ClientManager.instance().getModelFacade().addObserver(this);
	}
	
	public ILoginView getLoginView() {
		
		return (ILoginView)super.getView();
	}
	
	public IMessageView getMessageView() {
		
		return messageView;
	}
	
	/**
	 * Sets the action to be executed when the user logs in
	 * 
	 * @param value The action to be executed when the user logs in
	 */
	public void setLoginAction(IAction value) {
		
		loginAction = value;
	}
	
	/**
	 * Returns the action to be executed when the user logs in
	 * 
	 * @return The action to be executed when the user logs in
	 */
	public IAction getLoginAction() {
		
		return loginAction;
	}

	@Override
	public void start() {
		
		getLoginView().showModal();
	}

	@Override
	public void signIn() {
		
		String username = getLoginView().getLoginUsername();
		String password = getLoginView().getLoginPassword();
		
		Credentials cred = new Credentials(username, password);
		
		try {
			boolean login = ClientManager.instance().getServerProxy().login(cred);
			if (login) {
				// If log in succeeded
				getLoginView().closeModal();
				loginAction.execute();
			} else {
				getMessageView().setTitle("Warning");
				getMessageView().setMessage("Wrong username and password");
				getMessageView().showModal();
			}
		} catch (Exception e) {
			getMessageView().setTitle("Error");
			getMessageView().setMessage("Something went wrong while loggin in. " + e.getMessage());
			getMessageView().showModal();
		}
	}

	@Override
	public void register() {
		
		String username = getLoginView().getRegisterUsername();
		String password1 = getLoginView().getRegisterPassword();
		String password2 = getLoginView().getRegisterPasswordRepeat();
		
		if (password2.length() == 0) {
			getMessageView().setTitle("Warning");
			getMessageView().setMessage("Please confirm the password");
			getMessageView().showModal();
		} else if (!password1.equals(password2)) {
			getMessageView().setTitle("Warning");
			getMessageView().setMessage("Passwords don't match");
			getMessageView().showModal();
			
		} else if (password1.length() == 0) {
			getMessageView().setTitle("Warning");
			getMessageView().setMessage("Password cannot be blank");
			getMessageView().showModal(); 
		} else {
			Credentials cred = new Credentials(username,password1);
			
			try {
				boolean register = ClientManager.instance().getServerProxy().register(cred);
				if (register) {
					// If register succeeded
					getLoginView().closeModal();
					loginAction.execute();
				}
			} catch (Exception e) {
				getMessageView().setTitle("Error");
				getMessageView().setMessage("Something went wrong while register. " + e.getMessage());
				getMessageView().showModal();
			}
		}
		
		
	}

	@Override
	public void update(Observable o, Object arg) {

	}

}

