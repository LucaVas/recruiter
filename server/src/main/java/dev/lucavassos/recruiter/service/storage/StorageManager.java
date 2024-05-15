package dev.lucavassos.recruiter.service.storage;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import dev.lucavassos.recruiter.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class StorageManager {


    private final String projectId;
    private volatile Storage storage;

    public StorageManager(@Value("${google.storage.project.id}") String projectId) {
        this.projectId = projectId;
    }

    protected Storage getStorage() {
        if (storage == null) {
            synchronized (StorageManager.class) {
                if (storage == null) {
                    try {
                        Credentials creds = GoogleCredentials.getApplicationDefault();
                        storage = StorageOptions.newBuilder()
                                .setProjectId(projectId)
                                .setCredentials(creds)
                                .build()
                                .getService();
                    } catch (IOException e) {
                        log.error("Error while getting storage instance: {}", e.getMessage());
                        throw new ServerException("Error while initializing Storage.");
                    }
                }
            }
        }
        return storage;
    }
}
