package com.nookly.booking;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NooklyApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().load();
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		System.setProperty("JWT_ACCESS_EXP", dotenv.get("JWT_ACCESS_EXP"));
		System.setProperty("JWT_REFRESH_EXP", dotenv.get("JWT_REFRESH_EXP"));

		System.setProperty("YANDEX_GEOCODER_API_KEY", dotenv.get("YANDEX_GEOCODER_API_KEY"));

		SpringApplication.run(NooklyApplication.class, args);
	}

}
