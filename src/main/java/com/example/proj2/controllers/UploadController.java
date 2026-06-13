package com.example.proj2.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
            byte[] bytes = comprimirImagem(ficheiro.getBytes());
            Map<?, ?> result = cloudinary.uploader().upload(
                    bytes,
                    ObjectUtils.asMap("folder", "taca-lab")
            );
            String url = (String) result.get("secure_url");
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao fazer upload da imagem.");
        }
    }

    private byte[] comprimirImagem(byte[] original) throws IOException {
        BufferedImage img = ImageIO.read(new java.io.ByteArrayInputStream(original));
        if (img == null) return original;

        int maxDim = 1920;
        int w = img.getWidth(), h = img.getHeight();
        if (w > maxDim || h > maxDim) {
            double ratio = Math.min((double) maxDim / w, (double) maxDim / h);
            w = (int) (w * ratio);
            h = (int) (h * ratio);
            Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            resized.getGraphics().drawImage(scaled, 0, 0, null);
            img = resized;
        }

        // Se já é pequena o suficiente, devolve os bytes originais
        if (original.length <= 8 * 1024 * 1024) return original;

        // Comprimir como JPEG com qualidade 85%
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        javax.imageio.ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        javax.imageio.ImageWriteParam params = writer.getDefaultWriteParam();
        params.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionQuality(0.85f);
        writer.setOutput(ImageIO.createImageOutputStream(out));
        writer.write(null, new javax.imageio.IIOImage(img, null, null), params);
        writer.dispose();
        return out.toByteArray();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSize(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("Ficheiro demasiado grande. Tamanho máximo: 50MB.");
    }
}
