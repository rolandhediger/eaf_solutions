package ch.fhnw.edu.rental.test.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ch.fhnw.edu.rental.service.util.CacheAspect;


public class TestUtil {

	private static String[] DEFAULT_CONTEXT = {"business.xml", "datasource.xml"};
	
	static ApplicationContext context = null;
	
	public static ApplicationContext getSpringContext() throws Exception {
		context = new ClassPathXmlApplicationContext(DEFAULT_CONTEXT);
		DbInitializer dbinit = (DbInitializer)context.getBean("dbinit");
		dbinit.resetData(context);
		return context;
	}
	
	public static ApplicationContext getSpringContext(String[] contexts) throws Exception {
		context = new ClassPathXmlApplicationContext(contexts);
		DbInitializer dbinit = (DbInitializer)context.getBean("dbinit");
		dbinit.resetData(context);
		return context;
	}
	
	public static void setSupportForLazyLoading() {
		CacheAspect.open();
	}

	public static void closeSupportForLazyLoading() {
		CacheAspect.close();
	}

}
