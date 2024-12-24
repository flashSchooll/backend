package com.flashcard;

import com.flashcard.constants.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

import java.util.Locale;
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableCaching
public class FkApplication {

	public static void main(String[] args) {
		Locale.setDefault(Constants.TURKISH);
		SpringApplication.run(FkApplication.class, args);
	}

}
