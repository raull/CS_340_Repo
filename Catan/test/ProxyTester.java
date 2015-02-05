/**
	This class tests specifically the ServerProxy class.
 */
public class ProxyTester
{
	public ProxyTester()
	{

	}

	/*
	 * Tests are needed for all of the following functions. 
	 * All that needs to be tested is the connection between the Proxy and the Server
	 * Need to test that the connection can be made and the result gives us a 200 OK response.
	 * These tests should all be fairly similar.
	 */

	//need to make sure the server is running for all of these tests

	private ServerProxy testProxy;

	@Before
	public void setUp()
	{
		String host;
		String port;

		testProxy = new ServerProxy(host, port);
	}

	@Test
	public void testLogin()
	{


		//testProxy.login(urlPath, new Object());
	}

	@Test
	public void testRegister()
	{

	}

	@Test
	public void testList()
	{
		try
		{
			assertNotNull(testProxy.list());
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testCreate()
	{
		try
		{
			assertNotNull(testProxy.create(new CreateGameRequest()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testJoin()
	{
		try
		{
			assertNotNull(testProxy.join(new JoinGameRequest()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testSave()
	{

	}

	@Test
	public void testLoad()
	{

	}

	@Test
	public void testModel()
	{
		try
		{
			assertNotNull(testProxy.model(1));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testReset()
	{
		try
		{
			assertNotNull(testProxy.reset());
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testSendChat()
	{
		try
		{
			assertNotNull(testProxy.sendChat(new SendChat()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testRollNumber()
	{
		try
		{
			assertNotNull(testProxy.rollNumber(new RollNumber()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testRobPlayer()
	{
		try
		{
			assertNotNull(testProxy.robPlayer(new RobPlayer()));
		}
		catch (ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testFinishTurn()
	{
		try
		{
			assertNotNull(testProxy.finishTurn(new FinishMove()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testBuyDevCard()
	{
		try
		{
			assertNotNull(testProxy.buyDevCard(new BuyDevCard()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testYearOfPlenty()
	{
		try
		{
			assertNotNull(testProxy.Year_of_Plenty(new Year_of_Plenty_()));
		}
		catch (ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testRoadBuilding()
	{
		try
		{
			assertNotNull(testProxy.Road_Building(new Road_Building_()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testSoldier()
	{
		try
		{
			assertNotNull(testProxy.Soldier(new Soldier_()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testMonopoly()
	{
		try
		{
			assertNotNull(testProxy.Monopoly(new Monopoly_()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testMonument()
	{
		try
		{
			assertNotNull(testProxy.Monument(new Monument_()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testBuildRoad()
	{
		try
		{
			assertNotNull(testProxy.buildRoad(new BuildRoad()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testBuildSettlement()
	{
		try
		{
			assertNotNull(testProxy.buildSettlement(new BuildSettlement()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testBuildCity()
	{
		try
		{
			assertNotNull(testProxy.buildCity(new BuildCity()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testOfferTrade()
	{
		try
		{
			assertNotNull(testProxy.offerTrade(new OfferTrade()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testAcceptTrade()
	{
		try
		{
			assertNotNull(testProxy.acceptTrade(new AcceptTrade()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testMaritimeTrade()
	{
		try
		{
			assertNotNull(testProxy.maritimeTrade(new MaritimeTrade()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testDiscardCards()
	{
		try
		{
			assertNotNull(testProxy.discardCards(new DiscardCards()));
		}
		catch(ProxyException e)
		{
			fail();
		}
	}

	@Test
	public void testChangeLogLevel()
	{

	}

	@Test
	public void testAddAI()
	{

	}

	@Test
	public void testListAI()
	{
		try
		{
			assertNotNull(testProxy.listAI());
		}
		catch(ProxyException e)
		{
			fail();
		}
	}
}