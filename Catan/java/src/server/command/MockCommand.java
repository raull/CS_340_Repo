package server.command;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import server.exception.ServerInvalidRequestException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

/**
 * Mock command class that is used in testing. Created by MockCommandFactory, passes back impostor JSON
 * @author thyer
 *
 */
public class MockCommand extends ServerCommand{
	boolean list = false;

	public MockCommand(HttpExchange arg0) {
		super(arg0);
		System.out.println("HttpContext: " + arg0.getHttpContext().getPath());
		System.out.println("URI: " + arg0.getRequestURI().toString());
		String uri = arg0.getRequestURI().toString();
		if(uri.equals("/user/register")||uri.equalsIgnoreCase("/user/login")){
			//include login cookie in JSON
			try{
				String encoded = getEncodedLoginCookie("Sam", "sam", "0");
				arg0.getResponseHeaders().add("Set-cookie", encoded);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		else if(uri.equals("games/list")){
			list = true;
		}
		else if(uri.equals("/games/join")){
			//include join game cookie in JSON
			try{
				String encoded = this.getEncodedJoinGameCookie("1");
				arg0.getResponseHeaders().add("Set-cookie", encoded);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		JsonElement output;
		Gson gson = new Gson();
		output = gson.fromJson("test", JsonElement.class);
		if(list){
			output = gson.fromJson(this.getExampleListString(), JsonElement.class);
		}
		return output;
	}
	
	/**
	 * Name pretty much says it all. Creates an encoded cookie for us, the required fields are in the paramaters
	 * @param name
	 * @param password
	 * @param playerID
	 * @return a string representation of the encoded login cookie
	 * @throws UnsupportedEncodingException
	 */
	private String getEncodedLoginCookie(String name, String password, String playerID) throws UnsupportedEncodingException{
		String plaintext = "{\"name\":\"" + name + "\",\"password\":\"" + password + "\",\"playerID\":" + playerID + "}";
		String encoded = URLEncoder.encode(plaintext, "UTF-8");
		encoded = "catan.user=" + encoded + ";Path=/;";
		//System.out.println("Plaintext cookie: " + plaintext);
		//System.out.println("Encoded cookie: " + encoded);
		return encoded;
	}
	
	private String getEncodedJoinGameCookie(String gameID){
		return "catan.game=" + gameID + ";Path=/;";
	}
	
	private String getExampleListString(){
		String output = "[\n\t{\n\t}\n]";
		return output;
	}

}
