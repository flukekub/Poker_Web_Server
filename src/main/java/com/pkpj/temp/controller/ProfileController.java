package com.pkpj.temp.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pkpj.temp.dtos.UploadResultDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
     @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.serviceRoleKey}")
    private String supabaseServiceRoleKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    @PostMapping("/upload")
    public ResponseEntity<UploadResultDto> uploadProfilePicture(
        @RequestParam("file") MultipartFile file,
        @RequestParam("userId") String userId
    ) throws IOException {

        String filePath = "user-profile/" + userId + ".jpg";
        byte[] fileBytes = file.getBytes();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(supabaseUrl + "/storage/v1/object/" + bucketName + "/" + filePath))
            .header("Authorization", "Bearer " + supabaseKey)
            .header("Content-Type", file.getContentType())
            .header("x-upsert", "true")
            .PUT(HttpRequest.BodyPublishers.ofByteArray(fileBytes))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> uploadResponse;
        try {
            uploadResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new UploadResultDto(null, "Upload interrupted: " + e.getMessage()));
        }

        if (uploadResponse.statusCode() == 200 || uploadResponse.statusCode() == 201) {
            String signUrlApi = supabaseUrl + "/storage/v1/object/sign/" + bucketName + "/" + filePath;
            HttpResponse<String> signUrlResponse;
            String jsonBody = "{\"expiresIn\":315360000}";
            HttpRequest signUrlRequest = HttpRequest.newBuilder()
                    .uri(URI.create(signUrlApi))
                    .header("Authorization", "Bearer " + supabaseServiceRoleKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            try {
                signUrlResponse = client.send(signUrlRequest, HttpResponse.BodyHandlers.ofString());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new UploadResultDto(null, "Signed URL request interrupted: " + e.getMessage()));
            }

            if (signUrlResponse.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(signUrlResponse.body());
                String signedUrl = jsonNode.get("signedURL").asText();
                String Url = supabaseUrl + "/storage/v1" + signedUrl;
                return ResponseEntity.ok(new UploadResultDto(filePath, Url));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new UploadResultDto(null, "Failed to get signed URL: " + signUrlResponse.body()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new UploadResultDto(null, "Upload failed: " + uploadResponse.body()));
        }
    }
}
