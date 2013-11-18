package ch.fhnw.edu.rental.aspects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateAspect {
	private static final Logger LOG = LoggerFactory
			.getLogger(TemplateAspect.class);

	public void doSomething() {
		LOG.debug("doSomehting was called(");
	}
}
