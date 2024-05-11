package dev.lucavassos.recruiter.service.storage.fileupload;

import com.google.cloud.storage.*;
import dev.lucavassos.recruiter.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
public class ResumeHandler {

    @Value("${google.storage.bucket.name}")
    private String bucket;

    @Autowired
    private StorageManager storageManager;

    @Autowired
    private StorageService storageService;

    public void uploadResume(InputStream fileStream, String fileName, UUID uniqueId, String candidatePan) {
        Storage storage = storageManager.getStorage();

        String folderPath = "users/" + candidatePan;
        storage.create(
                BlobInfo.newBuilder(bucket, folderPath).build(),
                new byte[0]
        );

        BlobId blobId = BlobId.of(bucket, folderPath + "/" + uniqueId.toString() + "/" + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        storageService.upload(storage, blobInfo, fileStream);
        log.info("Resume uploaded successfully as {}", blobId.getName());
    }

    public void deleteResume(UUID uniqueId, String candidatePan) {
        Storage storage = storageManager.getStorage();
        String folderPath = "users/" + candidatePan + "/" + uniqueId.toString();
        BlobId blobId = BlobId.of(bucket, folderPath);
        Boolean deleted = storageService.delete(storage, blobId);
        if (!deleted) {
            log.error("Error while deleting resume from {}", blobId.getName());
            throw new ServerException("Error while deleting resume. Please try again later.");
        } else log.info("Resume deleted successfully from {}", blobId.getName());
    }
}
