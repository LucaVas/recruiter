package dev.lucavassos.recruiter.skill.repository;

import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static dev.lucavassos.recruiter.testUtils.RandomUtils.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class SkillRepositoryTests {

    @Autowired
    private SkillRepository repository;

    @Autowired
    private TestEntityManager manager;


    @Test
    public void save_creates_new_skill() {

        String skillName = randomString(10, 50);
        Skill skill = Skill.builder()
                .name(skillName)
                .build();

        Skill savedSkill = repository.save(skill);

        Skill existsSkill = manager.find(Skill.class, savedSkill.getId());

        assertThat(existsSkill.getName()).isEqualTo(skillName);
    }

    @Test
    public void save_throws_error_if_skill_text_is_invalid() {
        Skill skillWithShortName = Skill.builder()
                .name("")
                .build();

        assertThatThrownBy(() -> {
            repository.save(skillWithShortName);
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Skill name must be at least 1 character long");
    }
}
