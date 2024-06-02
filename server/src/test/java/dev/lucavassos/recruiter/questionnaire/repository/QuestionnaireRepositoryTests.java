package dev.lucavassos.recruiter.questionnaire.repository;

import dev.lucavassos.recruiter.modules.client.domain.Industry;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.questionnaire.entity.Questionnaire;
import dev.lucavassos.recruiter.modules.questionnaire.entity.QuestionnaireId;
import dev.lucavassos.recruiter.modules.questionnaire.repository.QuestionnaireRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static dev.lucavassos.recruiter.testUtils.RandomUtils.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Rollback(false)
public class QuestionnaireRepositoryTests {

    @Autowired
    private QuestionnaireRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void save_Creates_NewQuestionnaire() {

        Client client = Client.builder()
                .name("Test Client")
                .industry(Industry.IT)
                .build();
        clientRepository.saveAndFlush(client);

        QuestionnaireId id = QuestionnaireId.builder()
                .title("Test Questionnaire")
                .clientName(client.getName())
                .build();
        Questionnaire questionnaire = Questionnaire.builder()
                .id(id)
                .client(client)
                .build();

        repository.saveAndFlush(questionnaire);
        Optional<Questionnaire> existsQuestionnaire = repository.findById(id);

        assertThat(existsQuestionnaire).isPresent();
        assertThat(existsQuestionnaire.get().getId()).isEqualTo(id);
        assertThat(existsQuestionnaire.get().getClient().getName()).isEqualTo(client.getName());
    }
}
