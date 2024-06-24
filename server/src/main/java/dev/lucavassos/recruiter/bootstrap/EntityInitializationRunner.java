package dev.lucavassos.recruiter.bootstrap;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EntityInitializationRunner implements ApplicationRunner {

    private final EntityInitializer entityInitializer;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
//        entityInitializer.createRoles();
//        entityInitializer.createUsers();
//        entityInitializer.createClients();
//        entityInitializer.saveSkills();
//        entityInitializer.saveQuestionnaires();
//        entityInitializer.saveJobs();
//        entityInitializer.saveCandidates();
    }
}
