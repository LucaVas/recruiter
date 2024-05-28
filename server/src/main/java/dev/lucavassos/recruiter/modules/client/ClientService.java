package dev.lucavassos.recruiter.modules.client;

import dev.lucavassos.recruiter.exception.BadRequestException;
import dev.lucavassos.recruiter.exception.DatabaseException;
import dev.lucavassos.recruiter.modules.client.domain.NewClientRequest;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDto;
import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientDtoMapper clientDtoMapper;

    @Transactional
    public List<ClientDto> getAllClients(Integer pageNumber, Integer pageSize) {
        log.debug("Retrieving {} clients", pageSize);

        Pageable limit = PageRequest.of(pageNumber, pageSize);
        List<ClientDto> clients =
                clientRepository.findAll(limit)
                        .stream()
                        .map(clientDtoMapper)
                        .toList();

        log.debug("Clients retrieved: {} ({})", clients, clients.size());

        return clients;
    }

    @Transactional
    public ClientDto addClient(NewClientRequest request) {
        log.debug("Adding client: {}", request);

        if (clientRepository.existsByName(request.name())) {
            throw new BadRequestException("Client already exists");
        }

        Client newClient = Client.builder().name(request.name()).industry(request.industry()).build();
        ClientDto client = clientDtoMapper.apply(saveClient(newClient));

        log.debug("Client added: {}", client);

        return client;
    }

    @Transactional
    private Client saveClient(Client client) {
        try {
            return clientRepository.save(client);
        } catch (Exception e) {
            log.error("Database error while adding comment to candidacy: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }
}
