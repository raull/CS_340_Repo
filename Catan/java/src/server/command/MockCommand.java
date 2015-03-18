package server.command;

import java.net.URLEncoder;

import server.exception.ServerInvalidRequestException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

public class MockCommand extends ServerCommand{
	boolean sendCookie = false;

	public MockCommand(HttpExchange arg0) {
		super(arg0);
		System.out.println("HttpContext: " + arg0.getHttpContext().getPath());
		System.out.println("URI: " + arg0.getRequestURI().toString());
		String uri = arg0.getRequestURI().toString();
		if(uri.equals("/user/register")||uri.equalsIgnoreCase("/user/login")){
			sendCookie = true;
		}
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		JsonElement output;
		Gson gson = new Gson();
		if(sendCookie){
			//include cookie in JSON
			try{
				String encoded = URLEncoder.encode("{\"name\":\"Sam\",\"password\":\"sam\",\"playerID\":0}", "UTF-8");
				System.out.println(encoded);
				output = gson.fromJson(encoded,  JsonElement.class);
				return output;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub
		output = gson.fromJson("test", JsonElement.class);
		return output;
	}

}
