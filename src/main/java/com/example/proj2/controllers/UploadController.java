package com.example.proj2.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    /**
     * POST /api/upload/imagem
     * Form-data: campo "ficheiro" com a imagem
     * Devolve: { "url": "/uploads/nome-gerado.jpg" }
     */
    @PostMapping("/imagem")
    public ResponseEntity<?> uploadImagem(@RequestParam("ficheiro") MultipartFile ficheiro) {
        if (ficheiro == null || ficheiro.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhum ficheiro enviado.");
        }

        String contentType = ficheiro.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body("Apenas imagens são permitidas.");
        }

        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            String extensao = obterExtensao(ficheiro.getOriginalFilename());
            String nomeUnico = UUID.randomUUID() + extensao;
            Path destino = dir.resolve(nomeUnico);
            Files.copy(ficheiro.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            String url = "/uploads/" + nomeUnico;
            return ResponseEntity.ok(Map.of("url", url));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao guardar o ficheiro.");
        }
    }

    private String obterExtensao(String nomeOriginal) {
        if (nomeOriginal == null || !nomeOriginal.contains(".")) return ".jpg";
        return nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
    }
}
