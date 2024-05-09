package dev.lucavassos.recruiter.modules.client;

import dev.lucavassos.recruiter.modules.client.domain.NewClientRequest;
import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "api/v1")
public class ClientController {

    @Autowired
    private ClientService service;


    @GetMapping("/clients")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        log.info("Received request for all clients.");
        return new ResponseEntity<>(service.getAllClients(0, 10), HttpStatus.OK);
    }

    @PostMapping("/clients")
    public ResponseEntity<ClientDto> addClient(
            @Valid @RequestBody NewClientRequest request) {
        log.info("Received request to add client: {}", request);
        return new ResponseEntity<>(service.addClient(request), HttpStatus.CREATED);
    }
}
