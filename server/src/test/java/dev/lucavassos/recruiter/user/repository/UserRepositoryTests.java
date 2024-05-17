package dev.lucavassos.recruiter.user.repository;

import dev.lucavassos.recruiter.modules.user.domain.RoleName;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.RoleRepository;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static dev.lucavassos.recruiter.testUtils.RandomUtils.randomEmail;
import static dev.lucavassos.recruiter.testUtils.RandomUtils.randomPhoneNumber;
import static dev.lucavassos.recruiter.testUtils.RandomUtils.randomLowerCaseString;
import static dev.lucavassos.recruiter.testUtils.RandomUtils.randomString;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TestEntityManager manager;


    @Test
    public void save_creates_new_user() {

        // GIVEN
        Role recruiterRole = roleRepository.findByName(RoleName.RECRUITER).orElseThrow();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email(randomEmail())
                .password(encoder.encode("Password123"))
                .phone(randomPhoneNumber())
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .role(recruiterRole)
                .build();

        // WHEN
        User savedUser = userRepository.save(user);
        User existsUser = manager.find(User.class, savedUser.getId());

        // THEN
        assertThat(existsUser.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(existsUser.getPassword()).startsWith("$2");
        assertThat(existsUser.getRole()).isEqualTo(recruiterRole);
    }

    @Test
    public void save_throws_error_if_email_exists() {

        Role recruiterRole = roleRepository.findByName(RoleName.RECRUITER).orElseThrow();
        User user1 = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email("user@mail.com")
                .password(randomString(8, 64))
                .phone(randomPhoneNumber())
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .role(recruiterRole)
                .build();

        User user2 = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email("user@mail.com")
                .password(randomString(8, 64))
                .phone(randomPhoneNumber())
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .role(recruiterRole)
                .build();

        userRepository.save(user1);

        assertThatThrownBy(() -> {
            userRepository.save(user2);
        }).isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("duplicate key value violates unique constraint \"users_email_key");
    }

    @Test
    public void save_throws_error_if_phone_exists() {

        Role recruiterRole = roleRepository.findByName(RoleName.RECRUITER).orElseThrow();

        User user1 = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email(randomEmail())
                .password("Password123!!")
                .phone("0000000000")
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .role(recruiterRole)
                .build();

        User user2 = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email(randomEmail())
                .password("Password123!!")
                .phone("0000000000")
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .role(recruiterRole)
                .build();

        userRepository.save(user1);

        assertThatThrownBy(() -> {
            userRepository.save(user2);
        }).isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("duplicate key value violates unique constraint \"users_phone_key");
    }

    @Test
    public void save_throws_error_if_phone_is_invalid() {

        Role recruiterRole = roleRepository.findByName(RoleName.RECRUITER).orElseThrow();
        User userWithShortPhone = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email(randomEmail())
                .password(randomString(8, 64))
                .phone("")
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .role(recruiterRole)
                .build();

        User userWithLongPhone = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email(randomEmail())
                .password(randomString(8, 64))
                .phone("12345678901")
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .role(recruiterRole)
                .build();

        assertThatThrownBy(() -> {
            userRepository.save(userWithShortPhone);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Phone number must be 10 characters long");
        assertThatThrownBy(() -> {
            userRepository.save(userWithLongPhone);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Phone number must be 10 characters long");
    }

    @Test
    public void save_throws_error_if_username_is_invalid() {
        User userWithShortUsername = User.builder()
                .name("")
                .email(randomEmail())
                .password(randomString(8, 64))
                .phone(randomPhoneNumber())
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .build();

        User userWithLongUsername = User.builder()
                .name(randomLowerCaseString(51, 100))
                .email(randomEmail())
                .password(randomString(8, 64))
                .phone(randomPhoneNumber())
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .build();

        assertThatThrownBy(() -> {
            userRepository.save(userWithShortUsername);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Username must be between 3 and 50 characters long");

        assertThatThrownBy(() -> {
            userRepository.save(userWithLongUsername);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Username must be between 3 and 50 characters long");
    }

    @Test
    public void save_throws_error_if_email_is_invalid() {
        User userWithWrongEmail = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email("mail.com")
                .password(randomString(8, 64))
                .phone(randomPhoneNumber())
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .build();

        assertThatThrownBy(() -> {
            userRepository.save(userWithWrongEmail);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Invalid email");
    }

    @Test
    public void save_throws_error_if_password_is_invalid() {
        User userWithInvalidPassword = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email(randomEmail())
                .password(randomLowerCaseString(8, 64))
                .phone(randomPhoneNumber())
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .build();

        User userWithShortPassword = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email(randomEmail())
                .password(randomString(1, 7))
                .phone(randomPhoneNumber())
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .build();

        User userWithLongPassword = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email(randomEmail())
                .password(randomString(65, 100))
                .phone(randomPhoneNumber())
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(3, 8))
                .build();

        assertThatThrownBy(() -> {
            userRepository.save(userWithInvalidPassword);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Invalid password. Password must contain at least 1 lowercase letter, 1 uppercase letter and 1 number.");
        assertThatThrownBy(() -> {
            userRepository.save(userWithShortPassword);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Password must be between 8 and 64 characters long");
        assertThatThrownBy(() -> {
            userRepository.save(userWithLongPassword);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Password must be between 8 and 64 characters long");
    }

    @Test
    public void save_throws_error_if_city_is_invalid() {
        User userWithShortCity = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email(randomEmail())
                .password(randomString(8, 64))
                .phone(randomPhoneNumber())
                .city("")
                .country(randomLowerCaseString(3, 8))
                .build();

        User userWithLongCity = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email(randomEmail())
                .password(randomString(8, 64))
                .phone(randomPhoneNumber())
                .city(randomLowerCaseString(51, 100))
                .country(randomLowerCaseString(3, 8))
                .build();

        assertThatThrownBy(() -> {
            userRepository.save(userWithShortCity);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("City name must be between 3 and 50 characters long");
        assertThatThrownBy(() -> {
            userRepository.save(userWithLongCity);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("City name must be between 3 and 50 characters long");
    }

    @Test
    public void save_throws_error_if_country_is_invalid() {
        User userWithShortCountry = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email(randomEmail())
                .password(randomString(8, 64))
                .phone(randomPhoneNumber())
                .city(randomLowerCaseString(3, 8))
                .country("")
                .build();

        User userWithLongCountry = User.builder()
                .name(randomLowerCaseString(3, 8))
                .email(randomEmail())
                .password(randomString(8, 64))
                .phone(randomPhoneNumber())
                .city(randomLowerCaseString(3, 8))
                .country(randomLowerCaseString(51, 100))
                .build();

        assertThatThrownBy(() -> {
            userRepository.save(userWithShortCountry);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Country name must be between 3 and 50 characters long");
        assertThatThrownBy(() -> {
            userRepository.save(userWithLongCountry);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Country name must be between 3 and 50 characters long");
    }

}
