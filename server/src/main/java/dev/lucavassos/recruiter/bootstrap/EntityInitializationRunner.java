package dev.lucavassos.recruiter.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class EntityInitializationRunner implements ApplicationRunner {

    private final EntityInitializer entityInitializer;

    @Override
    public void run(ApplicationArguments args) {
        try {
//            entityInitializer.createRoles();
//            entityInitializer.createUsers();
//            entityInitializer.createClients();
        } catch (Exception e) {
            log.error("Error during bootstrap: ", e);
        }

    }
}
