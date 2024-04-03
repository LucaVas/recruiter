package dev.lucavassos.recruiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class EntityInitializationRunner implements ApplicationRunner {

    @Autowired
    private EntityInitializer entityInitializer;

    @Override
    public void run(ApplicationArguments args) {
        entityInitializer.createRoles();
        entityInitializer.createContractTypes();
        entityInitializer.saveSkills();
        entityInitializer.saveJobs();
        entityInitializer.createUsers();
        entityInitializer.saveCandidates();
    }
}
