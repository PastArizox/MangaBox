package com.mangabox.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ServerApplication {

    static {
        Dotenv dotenv = Dotenv
                .configure()
                .ignoreIfMissing() // Don't crash if .env file is missing
                .load();

        System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL", "jdbc:postgresql://localhost:5432/default_db"));
        System.setProperty("DATABASE_USER", dotenv.get("DATABASE_USER", "default_user"));
        System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD", "default_password"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
