package dev.lucavassos.recruiter.user.repository;

import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TestEntityManager manager;


    @Test
    public void findAll_retrievesAllRoles_ifSaved() {

        // GIVEN
        Role recruiterRole = Role.builder().name(RoleName.RECRUITER).description("Recruiter").build();
        Role adminRole = Role.builder().name(RoleName.ADMIN).description("Admin").build();
        Role testerRole = Role.builder().name(RoleName.TESTER).description("Tester").build();
        roleRepository.saveAll(List.of(recruiterRole, adminRole, testerRole));

        // WHEN
        Optional<Role> recruiter = roleRepository.findByName(RoleName.RECRUITER);
        Optional<Role> admin = roleRepository.findByName(RoleName.ADMIN);
        Optional<Role> tester = roleRepository.findByName(RoleName.TESTER);

        // THEN
        assertThat(recruiter).isPresent();
        assertThat(admin).isPresent();
        assertThat(tester).isPresent();
        assertThat(recruiter.get().getName()).isEqualTo(RoleName.RECRUITER);
        assertThat(admin.get().getName()).isEqualTo(RoleName.ADMIN);
        assertThat(tester.get().getName()).isEqualTo(RoleName.TESTER);
    }

}
