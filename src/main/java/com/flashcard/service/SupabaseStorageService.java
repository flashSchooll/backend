package com.flashcard.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucket;

    private final HttpClient client = HttpClient.newHttpClient();

    public String uploadFile(MultipartFile file, String folderName)
            throws IOException, InterruptedException {

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        String uploadUrl = supabaseUrl
                + "/storage/v1/object/"
                + bucket
                + "/"
                + folderName
                + "/"
                + fileName;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uploadUrl))
                .header("Authorization", "Bearer " + supabaseKey)
                .header("apikey", supabaseKey)   // 🔥 BUNU EKLE
                .header("Content-Type", file.getContentType())
                .POST(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return fileName;
        } else {
            throw new RuntimeException("Upload failed: " + response.body());
        }
    }
}
