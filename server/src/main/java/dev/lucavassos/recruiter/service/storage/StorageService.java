package dev.lucavassos.recruiter.service.storage;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import dev.lucavassos.recruiter.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class StorageService {

    protected void upload(Storage storage, BlobInfo blobInfo, InputStream fileStream) {
        try {
            storage.createFrom(blobInfo, fileStream);
        } catch (IOException e) {
            log.error("Error while uploading file: {}", e.getMessage());
            throw new ServerException("Error while uploading file. Please try again later.");
        }
    }

    protected Boolean delete(Storage storage, BlobId blobId) {
        return storage.delete(blobId);
    }
}
