package com.yatharth.distributedurlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableKafka
public class DistributedUrlShortenerApplication {

	static void main(String[] args) {
		SpringApplication.run(DistributedUrlShortenerApplication.class, args);
	}

}
