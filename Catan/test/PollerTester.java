import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import client.poller.Poller;
import shared.model.facade.ModelFacade;
import shared.proxy.Moxy;
import shared.proxy.Proxy;

/**
	This class runs tests on the ServerPoller class.
 */

public class PollerTester
{
	private Poller testPoller;
	
	public PollerTester()
	{

	}



	@Before
	public void setUp()
	{
		Proxy testProxy = new Moxy();
		ModelFacade testFacade = new ModelFacade();
		testPoller = new Poller(testProxy, testFacade);
	}
	/*
	 *  Tests to verify that the Poller can update the Model
		Should really only need a few tests.
		Perhaps one where the model has not changed and one where it has.
	 */
	@Test
	public void testPollServer()
	{
		testPoller.pollServer();
		
		assertFalse(true);
		//test that the model has indeed been initialized with the information from model.json
		

	}

}