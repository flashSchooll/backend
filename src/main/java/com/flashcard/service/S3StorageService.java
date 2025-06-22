package com.flashcard.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.flashcard.model.enums.AWSDirectory;
import com.flashcard.service.excel.CustomMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
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
        MultipartFile multipartFile = zipFile(file);
        File convertedFile = convertMultipartFileToFile(multipartFile);

        String filename = UUID.randomUUID() + ".png";
        s3Client.putObject(new PutObjectRequest(bucketName, awsDirectory.path + filename, convertedFile));

        boolean isDeleted = convertedFile.delete();
        if (!isDeleted) {
            log.warn(String.format("%s silinemedi", file.getOriginalFilename()));
        }

        return baseUrl + awsDirectory.path + filename;

    }

    public String uploadFile(MultipartFile file, AWSDirectory awsDirectory, String lessonName) throws IOException {
        StringBuilder filename = new StringBuilder(UUID.randomUUID() + ".mp3");
        String awsBucketName = awsDirectory.path + lessonName + "/";

        File convertedFile = convertMultipartFileToFile(file);

        s3Client.putObject(new PutObjectRequest(bucketName, awsBucketName + filename, convertedFile));

        boolean isDeleted = convertedFile.delete();
        if (!isDeleted) {
            log.warn(String.format("%s silinemedi", file.getOriginalFilename()));
        }

        return baseUrl + awsBucketName + filename;
    }

    public String uploadFile(File file, AWSDirectory awsDirectory) {

        String filename = UUID.randomUUID() + ".png";

        s3Client.putObject(new PutObjectRequest(bucketName, awsDirectory.path + filename, file));

        boolean isDeleted = file.delete();
        if (!isDeleted) {
            log.warn(String.format("%s silinemedi", file.getName()));
        }

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

        try {
            s3Client.deleteObject(bucketName, filename.substring(baseUrl.length()));
        } catch (SdkClientException e) {
            log.error("Error deleting file from s3 {}", filename, e);
        }

    }


    public File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
            throw new IOException(e);
        }

        return convertedFile;
    }

    private MultipartFile zipFile(MultipartFile file) throws IOException {
        // Dosyanın boyutunu kontrol et (500 KB'dan küçükse, işlem yapma)
        if (file.getSize() < 500 * 1024) {
            return file; // Eğer dosya 500 KB'dan küçükse, orijinal dosyayı geri döndür
        }

        // Görüntüyü okuyun
        BufferedImage inputImage = ImageIO.read(file.getInputStream());
        if (inputImage == null) {
            throw new IllegalArgumentException("Invalid image file");
        }

        // Resmi yeniden boyutlandır
        int width = inputImage.getWidth() / 2;
        int height = inputImage.getHeight() / 2;
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, width, height, null);
        g2d.dispose();

        // JPEG formatında sıkıştırma için writer alın
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new UnsupportedOperationException("JPEG writer not available");
        }
        ImageWriter writer = writers.next();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.7f); // Başlangıç kalitesi
        }

        writer.write(null, new IIOImage(outputImage, null, null), param);
        writer.dispose();
        ios.close();
        byte[] imageData = baos.toByteArray();

        // Dosya boyutunu kontrol et ve kaliteyi azalt
        while (imageData.length > 500 * 1024) { // 500 KB civarına küçült
            baos.reset();
            param.setCompressionQuality(param.getCompressionQuality() - 0.05f); // Her seferinde kaliteyi biraz azalt
            if (param.getCompressionQuality() <= 0.1f) {
                throw new IOException("Cannot reduce image size below 500 KB");
            }
            writer.write(null, new IIOImage(outputImage, null, null), param);
            imageData = baos.toByteArray();
        }

        return new CustomMultipartFile(imageData, file.getOriginalFilename(), "image/jpeg");
    }

}
