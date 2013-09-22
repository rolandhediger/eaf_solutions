/**
 * 
 */
package eaf_u1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.hediger.roland.implementation.HelloWorldProvider;

/**
 * @author roland
 *
 */
public class HelloWorldTest {

	private HelloWorldProvider provider;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.provider = new HelloWorldProvider();
	}

	/**
	 * Test method for {@link edu.hediger.roland.implementation.HelloWorldProvider#getMessage()}.
	 */
	@Test
	public final void testGetMessage() {
		String message = provider.getMessage(); 
		assertEquals(message, "Hello World");
	}

}
