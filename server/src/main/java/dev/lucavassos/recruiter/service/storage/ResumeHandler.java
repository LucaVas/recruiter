package dev.lucavassos.recruiter.service.storage;

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

    public void deleteResume(UUID uniqueId, String candidatePan, String fileName) {
        Storage storage = storageManager.getStorage();
        String filePath = "users/" + candidatePan + "/" + uniqueId.toString() + "/" + fileName;
        Blob blob = storage.get(bucket, filePath);
        if (blob == null) {
            log.error("The file {} wasn't found in {}", fileName, bucket);
            throw new ServerException("The file is not available anymore.");
        }

        Storage.BlobSourceOption precondition =
                Storage.BlobSourceOption.generationMatch(blob.getGeneration());
        boolean deleted = storage.delete(bucket, filePath, precondition);
        if (!deleted) {
            log.error("Error while deleting resume from {}", blob.getName());
            throw new ServerException("Error while deleting resume. Please try again later.");
        } else log.info("Resume deleted successfully from {}", blob.getName());
    }
}
