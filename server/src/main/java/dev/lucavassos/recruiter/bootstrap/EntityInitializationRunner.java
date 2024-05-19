package dev.lucavassos.recruiter.bootstrap;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EntityInitializationRunner implements ApplicationRunner {

    private final EntityInitializer entityInitializer;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        entityManager.createNativeQuery("SELECT 1").getSingleResult();
//        entityInitializer.createRoles();
//        entityInitializer.createUsers();
//        entityInitializer.createClients();
//        entityInitializer.saveSkills();
//        entityInitializer.saveJobs();
//        entityInitializer.saveQuestions();
//        entityInitializer.saveCandidates();

    }
}
