package dev.lucavassos.recruiter.service.storage.fileupload;

import com.google.cloud.storage.*;
import dev.lucavassos.recruiter.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private StorageManager storageManager;

    public void uploadResume(InputStream fileStream, String fileName, UUID uniqueId, String candidatePan) {
        Storage storage = storageManager.getStorage();

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

    private void upload(Storage storage, BlobInfo blobInfo, InputStream fileStream) {
        try {
            storage.createFrom(blobInfo, fileStream);
        } catch (IOException e) {
            log.error("Error while uploading file: {}", e.getMessage());
            throw new ServerException("Error while uploading file. Please try again later.");
        }
    }
}
