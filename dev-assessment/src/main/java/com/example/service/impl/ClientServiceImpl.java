package com.example.service.impl;

import com.example.domain.Client;
import com.example.domain.ClientDto;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.ConflictException;
import com.example.exceptions.NotFoundException;
import com.example.repository.ClientRepository;
import com.example.service.ClientService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository repository) {
        this.clientRepository = repository;
    }
    @Override
    public ClientDto saveClient(ClientDto clientDto) {
        validate(clientDto);

        Client client = new Client(clientDto.getFirstName(), clientDto.getLastName(), clientDto.getMobileNumber(),
                clientDto.getIdNumber(), clientDto.getPhysicalAddress());

        boolean idNumberExists = clientRepository.existsByIdNumber(clientDto.getIdNumber());
        boolean mobileNumberExists = clientRepository.existsByMobileNumber(clientDto.getMobileNumber());

        if (idNumberExists) {
            throw new ConflictException("ID Number already exists");
        }

        if (mobileNumberExists) {
            throw new ConflictException("Mobile Number already exists");
        }

        client = clientRepository.save(client);
        return ClientDto.from(client);
    }



    public ClientDto updateClient(String idNumber, ClientDto clientDto) {
        // Check if a client with the requested ID exists
        Client client = clientRepository.findById(idNumber)
                .orElseThrow(() -> new NotFoundException("Client not found with ID: " + idNumber));

        // Update the client with the new data
        if (clientDto.getFirstName() != null) {
            client.setFirstName(clientDto.getFirstName());
        }

        if (clientDto.getLastName() != null) {
            client.setLastName(clientDto.getLastName());
        }

        if (clientDto.getMobileNumber() != null && !clientDto.getMobileNumber().equals(client.getMobileNumber())) {
            // Check if another client with the same mobile number already exists
            boolean mobileNumberExists = clientRepository.existsByMobileNumber(clientDto.getMobileNumber());
            if (mobileNumberExists) {
                throw new ConflictException("Client with mobile number " + clientDto.getMobileNumber() + " already exists");
            }

            client.setMobileNumber(clientDto.getMobileNumber());
        }

        if (clientDto.getIdNumber() != null && !clientDto.getIdNumber().equals(client.getIdNumber())) {
            // Validate the new ID number
            if (!isValidIdNumber(clientDto.getIdNumber())) {
                throw new BadRequestException("Invalid ID number: " + clientDto.getIdNumber());
            }

            // Check if another client with the same ID number already exists
            if (clientRepository.existsByIdNumber(clientDto.getIdNumber())) {
                throw new ConflictException("Client with ID number " + clientDto.getIdNumber() + " already exists");
            }

            client.setIdNumber(clientDto.getIdNumber());
        }

        if (clientDto.getPhysicalAddress() != null) {
            client.setPhysicalAddress(clientDto.getPhysicalAddress());
        }

        // Save the updated client
       return ClientDto.from(clientRepository.save(client));
    }


    @Override
    public List<ClientDto> search(String keyword) {
        List<Client> clients = clientRepository.findByFirstNameContainingOrIdNumberContainingOrMobileNumberContaining(
                keyword, keyword, keyword);

        return clients.stream()
                .map(ClientDto::from)
                .toList();

    }

    @Override
    public List<ClientDto> search(String firstName, String idNumber, String mobileNumber) {
        List<Client> clients = clientRepository.findByFirstNameContainingOrIdNumberContainingOrMobileNumberContaining(
                firstName, idNumber, mobileNumber);

        return clients.stream()
                .map(ClientDto::from)
                .toList();
    }

   public void deleteAll() {
        clientRepository.deleteAll();
    }


    private void validate(ClientDto client) {
        validateIdNumber(client.getIdNumber());
        validateMobileNumber(client.getMobileNumber());
    }

    private void validateIdNumber(String idNumber) {
        if (!isValidIdNumber(idNumber)) {
            throw new BadRequestException("Invalid ID number");
        }
        clientRepository.findByIdNumber(idNumber).ifPresent(c -> {
            throw new ConflictException("ID number already exists");
        });
    }

    private void validateMobileNumber(String mobileNumber) {
        if (mobileNumber == null) {
            return;
        }
        clientRepository.findByMobileNumber(mobileNumber).ifPresent(c -> {
            throw new ConflictException("Mobile number already exists");
        });
    }
    //luhn algorithm is used to validate the id number
    private boolean isValidIdNumber(String idNumber) {
        if (idNumber == null || idNumber.length() != 13) {
            return false;
        }
        try {
            long number = Long.parseLong(idNumber);
            int checkDigit = Integer.parseInt(idNumber.substring(12));
            int total = 0;
            for (int i = 0; i < 12; i++) {
                int digit = Integer.parseInt(idNumber.substring(i, i + 1));
                total += ((i % 2) == 0) ? digit : (digit * 2 > 9 ? digit * 2 - 9 : digit * 2);
            }
            return (10 - (total % 10)) % 10 == checkDigit;
        } catch (NumberFormatException e) {
            return false;
        }
    }




}
