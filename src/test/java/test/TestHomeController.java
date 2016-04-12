package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import next.controller.HomeController;

public class TestHomeController {

	private AnnotationConfigApplicationContext ac;
	
	@Before
	public void setUp() throws Exception {
		ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
	}

	@Test
	public void test() {
		HomeController hc1 = ac.getBean(HomeController.class);
		HomeController hc2 = ac.getBean(HomeController.class);
		assertTrue(hc1 == hc2);
	}
	
	@Test
	public void getBeanTest() {
		AnnotationConfigApplicationContext ACAC = new AnnotationConfigApplicationContext(ApplicationConfigBean.class);
		HomeController hc1 = ACAC.getBean(HomeController.class);
		HomeController hc2 = ACAC.getBean(HomeController.class);
		assertTrue(hc1 == hc2);
	}
	
	@After
	public void teardonw() {
		ac.close();
	}

}
