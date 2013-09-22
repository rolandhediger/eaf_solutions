package edu.hediger.roland;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import edu.hediger.roland.interfaces.MessageRenderer;


public class HelloWorldDecoupled {
  
	public static void main(String[] args) {
		BeanFactory factory  = getBeanFactory();
		MessageRenderer stdout = (MessageRenderer)factory.getBean("standard_out_renderer");
		stdout.render();
		MessageRenderer stderr_xml = (MessageRenderer)factory.getBean("standard_err_xml_renderer");
		stderr_xml.render();
	}

	private static BeanFactory getBeanFactory() {
		XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("helloconfig.xml"));
		return factory;
	}
}
