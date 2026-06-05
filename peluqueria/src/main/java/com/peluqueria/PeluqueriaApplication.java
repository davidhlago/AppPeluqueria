package com.peluqueria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PeluqueriaApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PeluqueriaApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(PeluqueriaApplication.class, args);
	}

}

// 16mb
// SET GLOBAL max_allowed_packet = 16777216;

// http://localhost:8080/swagger-ui/index.html#/