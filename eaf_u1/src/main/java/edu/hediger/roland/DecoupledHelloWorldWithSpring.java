package edu.hediger.roland;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.hediger.roland.implementation.StandardOutRenderer;

public class DecoupledHelloWorldWithSpring {

	public static void main(String[] args) {
		ApplicationContext context = getAppContext();
		StandardOutRenderer renderer = (StandardOutRenderer) context.getBean("standard_out_renderer");
		renderer.render();

	}

	private static ApplicationContext getAppContext() {
		ApplicationContext context = new ClassPathXmlApplicationContext("helloconfig.xml");
		
		return context;
	}

}
