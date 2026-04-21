package com.example.proj2;

import com.example.proj2.models.*;
import com.example.proj2.repository.ArtesaRepository;
import com.example.proj2.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Proj2Application {

    private static final Logger logger = LoggerFactory.getLogger(Proj2Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Proj2Application.class, args);
    }

    @Bean
    CommandLineRunner seedArtesa(ArtesaRepository artesaRepository) {
        return args -> {
            String email = "artesa.demo@tacalab.pt";
            if (artesaRepository.findByEmail(email).isEmpty()) {
                Artesa artesa = new Artesa();
                artesa.setNome("Artesa Demo");
                artesa.setEmail(email);
                artesa.setPassword("Artesa123!");
                artesaRepository.save(artesa);
                logger.info("Artesa seed criada: {}", email);
            }
        };
    }


}