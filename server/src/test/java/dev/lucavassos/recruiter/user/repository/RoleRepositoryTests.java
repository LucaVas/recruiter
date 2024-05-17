package dev.lucavassos.recruiter.user.repository;

import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static dev.lucavassos.recruiter.testUtils.RandomUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TestEntityManager manager;


    @Test
    public void findAll_retrievesAllRoles_ifSaved() {
        // WHEN
        Optional<Role> recruiter = roleRepository.findByName(RoleName.RECRUITER);
        Optional<Role> admin = roleRepository.findByName(RoleName.ADMIN);
        Optional<Role> tester = roleRepository.findByName(RoleName.TESTER);

        // THEN
        assertThat(recruiter).isPresent();
        assertThat(admin).isPresent();
        assertThat(tester).isPresent();
    }

}
