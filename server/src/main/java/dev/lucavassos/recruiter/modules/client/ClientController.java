package dev.lucavassos.recruiter.modules.client;

import dev.lucavassos.recruiter.modules.client.repository.dto.ClientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
public class ClientController {

    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientService service;


    @GetMapping("/clients")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        LOG.info("Received request for all clients.");
        return new ResponseEntity<>(service.getAllClients(), HttpStatus.OK);
    }
}
