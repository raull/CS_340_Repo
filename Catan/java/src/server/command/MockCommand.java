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

	public MockCommand(HttpExchange arg0) {
		super(arg0);
		System.out.println("HttpContext: " + arg0.getHttpContext().getPath());
		System.out.println("URI: " + arg0.getRequestURI().toString());
		String uri = arg0.getRequestURI().toString();
		if(uri.equals("/user/register")||uri.equalsIgnoreCase("/user/login")){
			//include cookie in JSON
			try{
				String encoded = getEncodedLoginCookie("Sam", "sam", "0");
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
		// TODO Auto-generated method stub
		output = gson.fromJson("test", JsonElement.class);
		return output;
	}
	
	private String getEncodedLoginCookie(String name, String password, String playerID) throws UnsupportedEncodingException{
		String plaintext = "{\"name\":\"" + name + "\",\"password\":\"" + password + "\",\"playerID\":" + playerID + "}";
		String encoded = URLEncoder.encode(plaintext, "UTF-8");
		encoded = "catan.user=" + encoded + ";Path=/;";
		System.out.println("Plaintext cookie: " + plaintext);
		System.out.println("Encoded cookie: " + encoded);
		return encoded;
	}

}
