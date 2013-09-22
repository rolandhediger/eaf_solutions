package edu.hediger.roland.implementation;

import edu.hediger.roland.interfaces.MessageProvider;
import edu.hediger.roland.interfaces.MessageRenderer;


public class StandardOutRenderer implements MessageRenderer {

	private MessageProvider mp;

	@Override
	public void render() {
		System.out.println("message: " + mp.getMessage());

	}

	@Override
	public void setMessageProvider(MessageProvider mp) {
		this.mp = mp;
		
	}

}
