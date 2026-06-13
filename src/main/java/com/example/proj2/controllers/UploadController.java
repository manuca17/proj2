package com.example.proj2.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private final Cloudinary cloudinary;

    public UploadController(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }

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
            Map<?, ?> result = cloudinary.uploader().upload(
                    ficheiro.getBytes(),
                    ObjectUtils.asMap("folder", "taca-lab")
            );
            String url = (String) result.get("secure_url");
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao fazer upload da imagem.");
        }
    }
}
