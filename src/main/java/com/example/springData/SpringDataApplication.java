package com.example.springData;

// SPRING DATA JDBC JPA WEB H2
// default data source = jdbc:h2:mem:testdb 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataApplication.class, args);
	}

}


