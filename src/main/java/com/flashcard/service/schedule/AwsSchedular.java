package com.flashcard.service.schedule;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.PodcastRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AwsSchedular {

    private final CardRepository cardRepository;
    private final PodcastRepository podcastRepository;
    private final AmazonS3 amazonS3;

    @Value("${application.bucket.name}")
    private String bucketName;

    @Scheduled(cron = "0 1 0 1 * ?", zone = "Europe/Istanbul") // todo düzenlenecek
    public void deleteAwsFiles() {

        List<String> frontPathList = cardRepository.findAllFrontPath();
        List<String> backPathList = cardRepository.findAllBackPath();
        List<String> podcastPaths = podcastRepository.findAllPath();

        List<String> allPaths = new ArrayList<>();
        allPaths.addAll(frontPathList);
        allPaths.addAll(backPathList);
        allPaths.addAll(podcastPaths);

        List<String> s3ObjectKeys = new ArrayList<>();
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName);
        ListObjectsV2Result result;

        do {
            result = amazonS3.listObjectsV2(req);
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                s3ObjectKeys.add(objectSummary.getKey());
            }
            req.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());

        Set<String> dbPathsSet = new HashSet<>(allPaths);
        List<String> unusedKeys = s3ObjectKeys.stream()
                .filter(key -> !dbPathsSet.contains(key))
                .toList();

        for (String key : unusedKeys) {
            amazonS3.deleteObject(bucketName, key);
            System.out.println(key + " deleted");
        }

    }


}
