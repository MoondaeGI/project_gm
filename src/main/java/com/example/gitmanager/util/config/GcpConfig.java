package com.example.gitmanager.util.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class GcpConfig {
    @Value("${gcp.bucket.key}")
    private String bucketKey;
    @Value("${gcp.bucket.project.id}")
    private String projectId;

    @Bean
    public Storage getStorage() throws IOException {
        ClassPathResource resource = new ClassPathResource(bucketKey);
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());

        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
