package com.yatharth.distributedurlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DistributedUrlShortenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributedUrlShortenerApplication.class, args);
	}

}
