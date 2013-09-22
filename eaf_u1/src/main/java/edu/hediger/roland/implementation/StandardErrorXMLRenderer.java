package edu.hediger.roland.implementation;

import edu.hediger.roland.interfaces.MessageProvider;
import edu.hediger.roland.interfaces.MessageRenderer;


public class StandardErrorXMLRenderer implements MessageRenderer {
	

	private MessageProvider mp;

	@Override
	public void render() {
		System.err.println("<error>" + mp.getMessage() + "</error>");

	}

	@Override
	public void setMessageProvider(MessageProvider mp) {
		this.mp = mp;
		
	}

}
