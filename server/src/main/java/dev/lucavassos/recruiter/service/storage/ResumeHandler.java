package dev.lucavassos.recruiter.service.storage;

import dev.lucavassos.recruiter.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
public class ResumeHandler {

    @Autowired
    private StorageService storageService;

    public void uploadResume(InputStream fileStream, String fileName, UUID uniqueId, String candidatePan) {
        String filePath = "users/" + candidatePan + "/" + uniqueId.toString() + "/" + fileName;
        storageService.upload(filePath, fileStream);
        log.info("Resume uploaded successfully to {}", filePath);
    }

    public void deleteResume(UUID uniqueId, String candidatePan, String fileName) {
        String filePath = "users/" + candidatePan + "/" + uniqueId.toString() + "/" + fileName;
        boolean deleted = storageService.delete(filePath);
        if (!deleted) {
            log.error("Error while deleting resume at {}", filePath);
            throw new ServerException("Error while deleting resume. Please try again later.");
        } else log.info("Resume deleted successfully at {}", filePath);
    }
}
