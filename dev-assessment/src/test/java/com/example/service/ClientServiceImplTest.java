package com.example.service;

import com.example.domain.Client;
import com.example.domain.ClientDto;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.ConflictException;
import com.example.repository.ClientRepository;
import com.example.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClientServiceImplTest {
@Mock
    private ClientRepository clientRepository;

    private ClientService clientService;
@Autowired
    private ClientServiceImpl clientServiceImpl;
private ClientDto clientDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        clientService = new ClientServiceImpl(clientRepository);
        clientDto = new ClientDto("John", "Doe", "0712345678", "8001015009087", "123 Main Street");
    }


    @Test
    void saveClient() {

        Client client = new Client(clientDto.getFirstName(), clientDto.getLastName(), clientDto.getMobileNumber(),
                clientDto.getIdNumber(), clientDto.getPhysicalAddress());

        when(clientRepository.existsByIdNumber(clientDto.getIdNumber())).thenReturn(false);
        when(clientRepository.existsByMobileNumber(clientDto.getMobileNumber())).thenReturn(false);
        when(clientRepository.save(client)).thenReturn(client);

        ClientDto savedClient = clientService.saveClient(clientDto);

        assertEquals(clientDto, savedClient);

    }

    @Test
    void saveClientWithExistingIdNumber() {

        when(clientRepository.existsByIdNumber(clientDto.getIdNumber())).thenReturn(true);

        assertThrows(ConflictException.class, () -> clientService.saveClient(clientDto));
    }

    @Test
    void saveClientWithExistingMobileNumber() {
        ClientDto clientDto = new ClientDto( "John", "Doe", "0712345678", "8001015009087", "123 Main Street");

        when(clientRepository.existsByIdNumber(clientDto.getIdNumber())).thenReturn(false);
        when(clientRepository.existsByMobileNumber(clientDto.getMobileNumber())).thenReturn(true);

        assertThrows(ConflictException.class, () -> clientService.saveClient(clientDto));
    }

    @Test
    void saveClientWithInvalidIdNumber() {
         clientDto.setIdNumber("1234567890123");

        assertThrows(BadRequestException.class, () -> clientService.saveClient(clientDto));
    }


}
