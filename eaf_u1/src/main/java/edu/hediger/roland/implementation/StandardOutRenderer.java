package edu.hediger.roland.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import edu.hediger.roland.interfaces.MessageProvider;
import edu.hediger.roland.interfaces.MessageRenderer;

@Component
public class StandardOutRenderer implements MessageRenderer {
	@Autowired
	@Qualifier("helloWorldProvider")
	private MessageProvider mp;

	@Override
	public void render() {
		System.out.println("message fetched using spring annotations: " + mp.getMessage());

	}

	
	@Override
	public void setMessageProvider(MessageProvider mp) {
		this.mp = mp;
		
	}

}
