package com.applaudostudios.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceServerApplication {
	public ResourceServerApplication() {
	}

	public static void main(final String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
	}
}
