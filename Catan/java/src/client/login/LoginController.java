package client.login;

import client.base.*;
import client.manager.ClientManager;
import client.misc.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import shared.proxy.user.Credentials;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


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
		
		// TODO: log in user
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
			getMessageView().setMessage("Something went wrong while loggin in. Please check connection and try again later.");
			getMessageView().showModal();
		}
	}

	@Override
	public void register() {
		
		// TODO: register new user (which, if successful, also logs them in)
		
		// If register succeeded
		getLoginView().closeModal();
		loginAction.execute();
	}

	@Override
	public void update(Observable o, Object arg) {
		// If log in succeeded
		getLoginView().closeModal();
		loginAction.execute();
	}

}

