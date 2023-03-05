package com.example.service;

import com.example.domain.Client;
import com.example.domain.ClientDto;

import java.util.List;

public interface ClientService {
    ClientDto saveClient(ClientDto client);
    public ClientDto updateClient(String id, ClientDto clientDto);
    List<ClientDto> search(String keyword);
    List<ClientDto> search(String firstName, String idNumber, String mobileNumber);

}
