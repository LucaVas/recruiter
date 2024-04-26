package dev.lucavassos.recruiter;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EntityInitializationRunner implements ApplicationRunner {

    private final EntityInitializer entityInitializer;

    @Override
    public void run(ApplicationArguments args) {
        entityInitializer.createRoles();
        entityInitializer.createUsers();
        entityInitializer.createClients();
        entityInitializer.saveSkills();
        entityInitializer.saveJobs();
        entityInitializer.saveCandidates();
    }
}
