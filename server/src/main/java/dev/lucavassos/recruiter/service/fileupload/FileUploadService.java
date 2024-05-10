package dev.lucavassos.recruiter.service.fileupload;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import dev.lucavassos.recruiter.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadService {

    @Value("${google.storage.bucket.name}")
    private String bucket;

    @Value("${google.storage.project.id}")
    private String projectId;

    public void uploadResume(InputStream fileStream, String fileName, UUID uniqueId, String candidatePan) {
        Storage storage = getStorage();

        String folderPath = "users/" + candidatePan;
        storage.create(
                BlobInfo.newBuilder(bucket, folderPath).build(),
                new byte[0]
        );

        BlobId blobId = BlobId.of(bucket, folderPath + "/" + uniqueId.toString() + "/" + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        upload(storage, blobInfo, fileStream);
        log.info("Resume uploaded successfully as {}", blobId.getName());
    }

    private Storage getStorage() {
        try {
            return StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build()
                    .getService();
        } catch (IOException e) {
            log.error("Error while getting storage instance: {}", e.getMessage());
            throw new ServerException("Error while uploading file. Please try again later.");
        }
    }

    private void upload(Storage storage, BlobInfo blobInfo, InputStream fileStream) {
        try {
            storage.createFrom(blobInfo, fileStream);
        } catch (IOException e) {
            log.error("Error while uploading file: {}", e.getMessage());
            throw new ServerException("Error while uploading file. Please try again later.");
        }
    }
}
