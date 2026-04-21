package com.example.proj2.config;

import com.example.proj2.models.Artesa;
import com.example.proj2.repository.ArtesaRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final String ADMIN_EMAIL = "admin@artesa.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_NOME = "Administrador";

    private final ArtesaRepository artesaRepository;

    public DataInitializer(ArtesaRepository artesaRepository) {
        this.artesaRepository = artesaRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        artesaRepository.findByEmail(ADMIN_EMAIL).ifPresentOrElse(
            existing -> System.out.println("[DataInitializer] Admin já existe: " + ADMIN_EMAIL),
            () -> {
                Artesa admin = new Artesa(ADMIN_NOME, ADMIN_EMAIL, ADMIN_PASSWORD);
                artesaRepository.save(admin);
                System.out.println("[DataInitializer] Admin criado com sucesso: " + ADMIN_EMAIL);
            }
        );
    }
}
