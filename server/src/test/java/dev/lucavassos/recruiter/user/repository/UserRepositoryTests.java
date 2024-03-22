package dev.lucavassos.recruiter.user.repository;

import dev.lucavassos.recruiter.modules.user.domain.Role;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class UserRepositoryTests {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager manager;


    @Test
    public void save_creates_new_user() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = new User(
                "username",
                "user@email.com",
                encoder.encode("Password123"),
                "1234567890",
                "city",
                "country",
                Role.RECRUITER
        );

        User savedUser = repository.save(user);

        User existsUser = manager.find(User.class, savedUser.getId());

        assertThat(existsUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(existsUser.getPassword()).startsWith("$2");
    }

    @Test
    public void save_throws_error_if_email_exists() {
        User user1 = new User(
                "username1",
                "user@email.com",
                "Password123",
                "1234567890",
                "city1",
                "country1",
                Role.RECRUITER
        );

        User user2 = new User(
                "username2",
                "user@email.com",
                "Password123",
                "1234567891",
                "city2",
                "country2",
                Role.RECRUITER
        );

        repository.save(user1);

        assertThatThrownBy(() -> {
            repository.save(user2);
        }).isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("duplicate key value violates unique constraint \"users_email_key");
    }

    @Test
    public void save_throws_error_if_mobile_exists() {
        User user1 = new User(
                "username1",
                "user1@email.com",
                "Password123",
                "1234567890",
                "city1",
                "country1",
                Role.RECRUITER
        );

        User user2 = new User(
                "username2",
                "user2@email.com",
                "Password123",
                "1234567890",
                "city2",
                "country2",
                Role.RECRUITER
        );

        repository.save(user1);

        assertThatThrownBy(() -> {
            repository.save(user2);
        }).isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("duplicate key value violates unique constraint \"users_mobile_key");
    }

    @Test
    public void save_throws_error_if_mobile_is_short_or_long() {
        User userWithShortMobile = new User(
                "username",
                "user@email.com",
                "Password123",
                "1234",
                "city",
                "country",
                Role.RECRUITER
        );

        User userWithLongMobile = new User(
                "username",
                "user@email.com",
                "Password123",
                "123456789010",
                "city",
                "country",
                Role.RECRUITER
        );

        assertThatThrownBy(() -> {
            repository.save(userWithShortMobile);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("size must be between 5 and 10");
        assertThatThrownBy(() -> {
            repository.save(userWithLongMobile);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("size must be between 5 and 10");
    }

    @Test
    public void save_throws_error_if_username_is_of_wrong_size() {
        User userWithShortUsername = new User(
                "",
                "user@email.com",
                "Password123",
                "1234567890",
                "city",
                "country",
                Role.RECRUITER
        );

        User userWithLongUsername = new User(
                "usernameusernameusernameusernameusernameusernameusernameusername",
                "user@email.com",
                "Password123",
                "1234567890",
                "city",
                "country",
                Role.RECRUITER
        );

        assertThatThrownBy(() -> {
            repository.save(userWithShortUsername);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("size must be between 3 and 50");

        assertThatThrownBy(() -> {
            repository.save(userWithLongUsername);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("size must be between 3 and 50");
    }

    @Test
    public void save_throws_error_if_email_is_invalid() {
        User userWithWrongEmail = new User(
                "username",
                "ut.com",
                "Password123",
                "1234567890",
                "city",
                "country",
                Role.RECRUITER
        );
        assertThatThrownBy(() -> {
            repository.save(userWithWrongEmail);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must be a well-formed email address");
    }

    @Test
    public void save_throws_error_if_password_is_invalid_short_or_long() {
        User userWithInvalidPassword = new User(
                "username",
                "username@mail.com",
                "password",
                "1234567890",
                "city",
                "country",
                Role.RECRUITER
        );

        User userWithShortPassword = new User(
                "username",
                "username@mail.com",
                "Pass1",
                "1234567890",
                "city",
                "country",
                Role.RECRUITER
        );

        User userWithLongPassword = new User(
                "username",
                "username@mail.com",
                "Password123Password123Password123Password123Password123Password123Password123Password123",
                "1234567890",
                "city",
                "country",
                Role.RECRUITER
        );
        assertThatThrownBy(() -> {
            repository.save(userWithInvalidPassword);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must match");
        assertThatThrownBy(() -> {
            repository.save(userWithShortPassword);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("size must be between 8 and 64");
        assertThatThrownBy(() -> {
            repository.save(userWithLongPassword);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("size must be between 8 and 64");
    }



}
