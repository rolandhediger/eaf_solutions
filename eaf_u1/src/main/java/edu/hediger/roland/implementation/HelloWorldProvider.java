package edu.hediger.roland.implementation;

import org.springframework.stereotype.Component;

import edu.hediger.roland.interfaces.MessageProvider;

@Component
public class HelloWorldProvider implements MessageProvider {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Hello World";
	}

}
