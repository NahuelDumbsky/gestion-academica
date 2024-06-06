package com.poo.act9poo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.poo.act9poo")
@EntityScan(basePackages = "com.poo.act9poo.entities")
public class Actividad9pooApplication {

	public static void main(String[] args) {
		SpringApplication.run(Actividad9pooApplication.class, args);
	}
}
