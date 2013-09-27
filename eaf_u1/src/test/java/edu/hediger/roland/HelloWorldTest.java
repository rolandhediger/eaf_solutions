package edu.hediger.roland;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.hediger.roland.interfaces.MessageProvider;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"helloconfig.xml"})
public class HelloWorldTest {

	@Autowired
	@Qualifier("helloWorldProvider")
	private MessageProvider provider;
	@Test
	public final void testGetMessage() {
		assertEquals("Hello World", provider.getMessage()); 
	}

}
