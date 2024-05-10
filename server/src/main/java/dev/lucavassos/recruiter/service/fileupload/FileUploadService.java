package dev.lucavassos.recruiter.service.fileupload;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileUploadService {

    private final GoogleStorageConfig googleStorageConfig = new GoogleStorageConfig();

    public void uploadResume() throws IOException {
        Storage storage = StorageOptions.newBuilder()
                .setProjectId("recruiter-420314")
                .setCredentials(
                        GoogleCredentials.fromStream(new FileInputStream("src/main/resources/secrets/recruiter-420314-a391a279757e.json")
                        )
                )
                .build()
                .getService();

        BlobId blobId = BlobId.of("recruiter-cv-bucket", "tmp/hello.txt");
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        String filePath = "src/main/resources/tmp/hello.txt";

        storage.createFrom(blobInfo, Paths.get(filePath));
        log.info("File {} uploaded to bucket {} as {}", filePath, googleStorageConfig.getBucket(), blobId.getName());
    }
}
