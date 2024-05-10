package dev.lucavassos.recruiter;

import dev.lucavassos.recruiter.security.RsaKeyProperties;
import dev.lucavassos.recruiter.service.fileupload.FileUploadService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class RecruiterApplication {

	public static void main(String[] args) {

		SpringApplication.run(RecruiterApplication.class, args);
		FileUploadService fileUploadService = new FileUploadService();
		try {
			fileUploadService.uploadResume();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
