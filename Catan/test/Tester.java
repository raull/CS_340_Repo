/**
	This class is intended to be an overall test class that manages
	and runs all other test classes and their associated JUnit tests.
*/
import org.junit.*;
import static org.junit.Assert.*;

public class Tester
{
	public static void main(String[] args)
	{
		String[] testClasses = new String[]{
			"ProxyTester", "PollerTester", "ModelTester"	
		};
		
		org.junit.runner.JUnitCore.main(testClasses);
	}
}