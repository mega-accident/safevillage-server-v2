package com.safevillage.safevillage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class SafevillageApplication {

  public static void main(String[] args) {
    SpringApplication.run(SafevillageApplication.class, args);
  }
}