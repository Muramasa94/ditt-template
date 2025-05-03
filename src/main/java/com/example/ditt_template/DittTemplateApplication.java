package com.example.ditt_template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DittTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(DittTemplateApplication.class, args);
		String route = "/converter/integer-base-converter"; // Example route, replace with actual route
		System.out.println();
		System.out.println("Application running on http://localhost:8080" + route);
		System.out.println();
	}

}
