package com.estore.api.estoreapi;

import com.estore.api.estoreapi.persistence.user.UserDAO;
import com.estore.api.estoreapi.persistence.user.UserFileDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class EstoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstoreApiApplication.class, args);
	}

}
