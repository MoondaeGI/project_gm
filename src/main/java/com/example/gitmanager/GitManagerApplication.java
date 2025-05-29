package com.example.gitmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GitManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(GitManagerApplication.class, args);
		System.out.println("spring start");
	}

}
