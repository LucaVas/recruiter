package dev.lucavassos.recruiter.modules.client.repository.dto;

import dev.lucavassos.recruiter.modules.client.entities.Client;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ClientDtoMapper implements Function<Client, ClientDto> {
    @Override
    public ClientDto apply(Client client) {
        return new ClientDto(
                client.getName(),
                client.getIndustry()
        );
    }
}
