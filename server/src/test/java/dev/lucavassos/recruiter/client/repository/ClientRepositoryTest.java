package dev.lucavassos.recruiter.client.repository;

import dev.lucavassos.recruiter.modules.client.domain.Industry;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest // set in-memory database
public class ClientRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void save_PersistClient_IfClientDoesNotExist () {
        Client client1 = new Client();
        client1.setName("Client1");
        client1.setIndustry(Industry.IT);

        entityManager.persist(client1);
    }

    @Test
    public void save_ThrowsException_IfClientNameExists() {
        Client client1 = new Client();
        client1.setName("Client1");
        client1.setIndustry(Industry.IT);

        Client client2 = new Client();
        client2.setName("Client1");
        client2.setIndustry(Industry.FINANCE);

        entityManager.persist(client1);
        entityManager.flush();

        assertThrows(EntityExistsException.class, () -> {
            entityManager.persist(client2);
            entityManager.flush();
        });
    }
}
