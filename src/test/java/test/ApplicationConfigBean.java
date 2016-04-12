package test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import next.controller.HomeController;

@Configuration
public class ApplicationConfigBean {
	@Bean
	public HomeController homeController() {
		return new HomeController();
	}
}


