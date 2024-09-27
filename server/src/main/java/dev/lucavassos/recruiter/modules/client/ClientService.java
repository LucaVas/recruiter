package dev.lucavassos.recruiter.modules.client;

import dev.lucavassos.recruiter.exception.BadRequestException;
import dev.lucavassos.recruiter.exception.DatabaseException;
import dev.lucavassos.recruiter.exception.ResourceNotFoundException;
import dev.lucavassos.recruiter.modules.HistoryEventType;
import dev.lucavassos.recruiter.modules.client.domain.NewClientRequest;
import dev.lucavassos.recruiter.modules.client.entities.Client;
import dev.lucavassos.recruiter.modules.client.entities.ClientHistory;
import dev.lucavassos.recruiter.modules.client.repository.ClientHistoryRepository;
import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDto;
import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDtoMapper;
import dev.lucavassos.recruiter.modules.user.entities.User;
import dev.lucavassos.recruiter.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientDtoMapper clientDtoMapper;
    private final UserRepository userRepository;
    private final ClientHistoryRepository historyRepository;

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

        Client newClient = saveClient(Client.builder().name(request.name()).industry(request.industry()).build());
        ClientDto client = clientDtoMapper.apply(newClient);

        log.debug("Client added: {}", client);

        saveClientHistoryEvent(newClient, HistoryEventType.CREATED);
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

    @Transactional
    private void saveClientHistoryEvent(Client client, HistoryEventType eventType) {
        User user = getAuthUser();
        try {
            ClientHistory event = ClientHistory.builder()
                    .name(client.getName())
                    .industry(client.getIndustry())
                    .eventType(eventType)
                    .client(client)
                    .modifiedBy(user)
                    .build();
            historyRepository.save(event);
        } catch (Exception e) {
            log.error("Database error while saving candidate history event: {}", e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }

    private User getAuthUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();
        return userRepository.findOneById(userPrincipal.getId()).orElseThrow(
                () -> {
                    log.error("User with id {} not found", userPrincipal.getId());
                    return new ResourceNotFoundException("User not found");
                }
        );
    }
}
