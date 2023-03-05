package com.example.web.rest;

import com.example.DevAssessmentApplication;
import com.example.domain.ClientDto;
import com.example.repository.ClientRepository;
import com.example.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {DevAssessmentApplication.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientService clientService;

    private ClientDto testClient;

    @Autowired
    ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        testClient = new ClientDto("John", "Doe", "0712345678", "8001015009087", "123 Main Street");
        clientService.saveClient(testClient);
    }

    @AfterEach
    public void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    public void testCreateClient() throws Exception {
        ClientDto newClient = new ClientDto("Jane", "Smith", "0823456789", "9506155800086", "456 Main Street");
        String json = objectMapper.writeValueAsString(newClient);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));
    }

    @Test
    public void testUpdateClient() throws Exception {
        testClient.setFirstName("Updated First Name");
        String json = objectMapper.writeValueAsString(testClient);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/clients/{id}", testClient.getIdNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());

        ClientDto updatedClient = ClientDto.from(clientRepository.findById(testClient.getIdNumber()).get());

        assertEquals(testClient.getFirstName(),updatedClient.getFirstName());
    }

    @Test
    public void testSearchClients() throws Exception {
        List<ClientDto> clientDtos = clientService.search("John", null, null);
        assertTrue(clientDtos.size() > 0);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/clients")
                        .param("firstName", "John")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(clientDtos.size()));
    }
}
