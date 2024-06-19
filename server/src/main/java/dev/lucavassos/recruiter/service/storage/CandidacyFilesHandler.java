package dev.lucavassos.recruiter.service.storage;

import dev.lucavassos.recruiter.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
public class CandidacyFilesHandler {

    @Autowired
    private StorageService storageService;

    public void upload(InputStream fileStream, String candidatePan, Long jobId, String fileName) {
        String filePath = getFilePath(candidatePan, jobId, fileName);
        storageService.upload(filePath, fileStream);
        log.info("File uploaded successfully to {}", filePath);
    }

    public void delete(String candidatePan, Long jobId, String fileName) {
        String filePath = getFilePath(candidatePan, jobId, fileName);
        boolean deleted = storageService.delete(filePath);
        if (!deleted) {
            log.error("Error while deleting file at {}", filePath);
            throw new ServerException("Error while deleting file. Please try again later.");
        } else log.info("File deleted successfully at {}", filePath);
    }

    public byte[] get(String candidatePan, Long jobId, String fileName) {
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
