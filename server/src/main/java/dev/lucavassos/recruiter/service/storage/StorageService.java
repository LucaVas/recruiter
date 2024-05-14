package dev.lucavassos.recruiter.service.storage;

import com.google.cloud.storage.*;
import dev.lucavassos.recruiter.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

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
        if (blob == null) {
            log.error("The file at {} wasn't found in {}", filePath, bucket);
            throw new ServerException("The file is not available anymore.");
        }
        Storage.BlobSourceOption precondition =
                Storage.BlobSourceOption.generationMatch(blob.getGeneration());
        return storage.delete(bucket, filePath, precondition);
    }

    private Blob getBlob(String filePath) {
        Storage storage = storageManager.getStorage();
        return storage.get(bucket, filePath);
    }

    protected URL getSignedUrl(String folderPath) {
        Storage storage = storageManager.getStorage();

        return storage.signUrl(
                createBlob(folderPath),
                15, TimeUnit.SECONDS,
                Storage.SignUrlOption.withV4Signature()
                );
    }

    private Blob createBlob(String folderPath) {
        Storage storage = storageManager.getStorage();
        return storage.create(BlobInfo.newBuilder(bucket, folderPath).build(), new byte[0]);
    }
}
