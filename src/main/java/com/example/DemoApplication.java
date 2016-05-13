package com.example;

import com.example.dao.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.io.File;

@SpringBootApplication
public class DemoApplication {

    public static final String UPLOADS_DIR = "uploads";
	public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
	}
	
    @Bean
    CommandLineRunner demo(final UserRepository ur) {
       return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                new File(UPLOADS_DIR).mkdirs();
            }
        };
    }
    
    

}


