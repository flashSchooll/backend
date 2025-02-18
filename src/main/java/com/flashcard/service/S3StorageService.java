package com.flashcard.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.flashcard.model.enums.AWSDirectory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class S3StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;
    @Value("${application.bucket.url}")
    private String baseUrl;

    private final AmazonS3 s3Client;

    public String uploadFile(MultipartFile file, AWSDirectory awsDirectory) throws IOException {
        File convertedFile = convertMultipartFileToFile(file);

        String filename = UUID.randomUUID() + ".png";
        s3Client.putObject(new PutObjectRequest(bucketName, awsDirectory.path + filename, convertedFile));

        convertedFile.delete();

        return baseUrl + awsDirectory.path + filename;

    }

    public byte[] downloadFile(String filename) {
        S3Object s3Object = s3Client.getObject(bucketName, filename);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error("Error converting to byte[] ", e);
        }
        return new byte[0];
    }

    public void deleteFile(String filename) {

        s3Client.deleteObject(bucketName, filename.substring(baseUrl.length()));

    }


    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
            throw new IOException(e);
        }

        return convertedFile;
    }
}
