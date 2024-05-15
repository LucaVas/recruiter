package dev.lucavassos.recruiter.modules.candidacy.domain;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CandidacyFilesUploadRequest(
        List<MultipartFile> files
) {
}
