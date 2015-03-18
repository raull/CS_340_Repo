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
			//include cookie in JSON
			try{
				String encoded = URLEncoder.encode("{\"name\":\"Sam\",\"password\":\"sam\",\"playerID\":0}", "UTF-8");
				System.out.println(encoded);
				arg0.getResponseHeaders().add("set-cookie", encoded);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public JsonElement execute() throws ServerInvalidRequestException {
		JsonElement output;
		Gson gson = new Gson();
		if(sendCookie){
			
		}
		// TODO Auto-generated method stub
		output = gson.fromJson("test", JsonElement.class);
		return output;
	}

}
