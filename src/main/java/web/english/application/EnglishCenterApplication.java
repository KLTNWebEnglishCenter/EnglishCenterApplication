package web.english.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@SpringBootApplication
public class EnglishCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnglishCenterApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

}