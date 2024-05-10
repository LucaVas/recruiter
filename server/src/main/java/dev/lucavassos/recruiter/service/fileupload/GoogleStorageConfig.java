package dev.lucavassos.recruiter.service.fileupload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleStorageConfig {

    @Value("${google.storage.bucket.name}")
    private String bucket;

    @Value("${google.storage.project.id}")
    private String projectId;

}
