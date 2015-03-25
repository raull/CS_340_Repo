package server;

import org.junit.Before;
import org.junit.Test;

import server.handler.Handler;

public class HandlerTester {
	private Handler handler;
	
	public HandlerTester(){
		handler = new Handler(false);
	}
	
	@Before
	public void setUp(){
		
	}
	
	@Test
	public void badRequests(){
		
	}
	
	@Test
	public void malformedJSON(){
		
	}
	
	@Test
	public void validRequests(){
		
	}

}
