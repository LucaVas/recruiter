package dev.lucavassos.recruiter.service.storage;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import dev.lucavassos.recruiter.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class StorageService {

    @Autowired
    private StorageManager storageManager;

    @Value("${google.storage.bucket.name}")
    private String bucket;

    protected void upload(String filePath, InputStream fileStream) {
        Storage storage = storageManager.getStorage();
        try {
            BlobId blobId = BlobId.of(bucket, filePath);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            Storage.BlobWriteOption precondition;
            if (storage.get(bucket, filePath) == null) precondition = Storage.BlobWriteOption.doesNotExist();
            else precondition = Storage.BlobWriteOption.generationMatch(storage.get(bucket, filePath).getGeneration());

            storage.createFrom(blobInfo, fileStream, precondition);
        } catch (IOException e) {
            log.error("Error while uploading file: {}", e.getMessage());
            throw new ServerException("Error while uploading file. Please try again later.");
        }
    }

    protected Boolean delete(String filePath) {
        Storage storage = storageManager.getStorage();

        Blob blob = this.getBlob(filePath);
        Storage.BlobSourceOption precondition =
                Storage.BlobSourceOption.generationMatch(blob.getGeneration());
        return storage.delete(bucket, filePath, precondition);
    }

    protected byte[] getFile(String filePath) {
        Storage storage = storageManager.getStorage();
        Blob blob = getBlob(filePath);
        log.info("Reading blob {}", blob);
        // Read blob content into byte array
        byte[] blobContent;
        blobContent = blob.getContent();
        return blobContent;
    }


    protected Blob getBlob(String filePath) {
        Storage storage = storageManager.getStorage();
        BlobId blobId = BlobId.of(bucket, filePath);
        Blob blob = storage.get(blobId);
        if (blob == null) {
            log.error("The file at {} wasn't found in {}", filePath, bucket);
            throw new ServerException("The file is not available anymore.");
        }
        return blob;
    }

    private Blob createBlob(String folderPath) {
        Storage storage = storageManager.getStorage();
        return storage.create(BlobInfo.newBuilder(bucket, folderPath).build(), new byte[0]);
    }
}
