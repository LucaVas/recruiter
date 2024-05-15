package dev.lucavassos.recruiter.service.storage;

import dev.lucavassos.recruiter.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;

@Slf4j
@Service
public class ResumeHandler {

    @Autowired
    private StorageService storageService;

    public void uploadResume(InputStream fileStream, String candidatePan, Long jobId, String fileName) {
        String filePath = getFilePath(candidatePan, jobId, fileName);
        storageService.upload(filePath, fileStream);
        log.info("Resume uploaded successfully to {}", filePath);
    }

    public void deleteResume(String candidatePan, Long jobId, String fileName) {
        String filePath = getFilePath(candidatePan, jobId, fileName);
        boolean deleted = storageService.delete(filePath);
        if (!deleted) {
            log.error("Error while deleting resume at {}", filePath);
            throw new ServerException("Error while deleting resume. Please try again later.");
        } else log.info("Resume deleted successfully at {}", filePath);
    }

    public byte[] getResume(String candidatePan, Long jobId, String fileName) {
        return storageService.getFile(getFilePath(candidatePan, jobId, fileName));
    }

    private String getFilePath(String candidatePan, Long jobId, String fileName) {
        return "candidates/" +
                candidatePan +
                "/job/" +
                jobId +
                "/" +
                fileName;
    }
}
