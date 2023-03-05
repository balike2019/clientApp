package com.example.web.rest;

import com.example.domain.ClientDto;
import com.example.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody ClientDto clientDto) {
        ClientDto savedClientDto = clientService.saveClient(clientDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedClientDto.getIdNumber())
                .toUri();
        return ResponseEntity.created(location).body(savedClientDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable String id, @Valid @RequestBody ClientDto clientDto) {
        ClientDto updatedClientDto = clientService.updateClient(id, clientDto);
        return ResponseEntity.ok(updatedClientDto);
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> searchClients(@RequestParam(required = false) String firstName,
                                                         @RequestParam(required = false) String idNumber,
                                                         @RequestParam(required = false) String mobileNumber) {
        List<ClientDto> clientDtos = clientService.search(firstName, idNumber, mobileNumber);
        return ResponseEntity.ok(clientDtos);
    }

}
