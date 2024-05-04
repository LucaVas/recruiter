package dev.lucavassos.recruiter.modules.client;

import dev.lucavassos.recruiter.modules.client.repository.ClientRepository;
import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDto;
import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDtoMapper;
import dev.lucavassos.recruiter.modules.question.repository.dto.QuestionDtoMapper;
import dev.lucavassos.recruiter.modules.skill.repository.SkillRepository;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDto;
import dev.lucavassos.recruiter.modules.skill.repository.dto.SkillDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientDtoMapper clientDtoMapper;

    @Transactional
    public List<ClientDto> getAllClients() {

        Pageable limit = PageRequest.of(0,10);
        LOG.info("Retrieving {} clients", limit.getPageSize());


        List<ClientDto> clients =
                clientRepository.findAll(limit)
                        .stream()
                        .map(client -> clientDtoMapper.apply(client)
                        )
                        .toList();

        LOG.info("Clients retrieved: {} ({})", clients, clients.size());

        return clients;
    }
}
