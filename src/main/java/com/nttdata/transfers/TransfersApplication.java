package com.nttdata.transfers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class TransfersApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransfersApplication.class, args);
	}

}
