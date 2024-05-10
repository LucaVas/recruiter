package dev.lucavassos.recruiter.service.fileupload;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Data
public class GoogleStorageConfig {

    @Value("${google.storage.bucket.name}")
    private String bucket;

    @Value("${google.storage.project.id}")
    private String projectId;

}
