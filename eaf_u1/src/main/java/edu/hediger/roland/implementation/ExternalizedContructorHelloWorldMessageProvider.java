package edu.hediger.roland.implementation;

import edu.hediger.roland.interfaces.MessageProvider;

public class ExternalizedContructorHelloWorldMessageProvider implements
		MessageProvider {

	private String message;
	public ExternalizedContructorHelloWorldMessageProvider(String message){
		this.message=message;
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}

}
