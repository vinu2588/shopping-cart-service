package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.domain.entity.Client;
import com.ecommerce.shoppingcart.domain.entity.IndividualClient;
import com.ecommerce.shoppingcart.domain.entity.ProfessionalClient;
import com.ecommerce.shoppingcart.exception.ClientNotFoundException;
import com.ecommerce.shoppingcart.exception.DuplicateClientException;
import com.ecommerce.shoppingcart.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing clients
 * Handles all client-related business operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public IndividualClient createIndividualClient(IndividualClient client) {
        log.debug("Creating individual client: {}", client.getClientId());
        
        if (clientRepository.existsByClientId(client.getClientId())) {
            throw new DuplicateClientException("Client with ID " + client.getClientId() + " already exists");
        }
        
        IndividualClient saved = clientRepository.save(client);
        log.info("Individual client created: {}", saved.getClientId());
        return saved;
    }

    @Transactional
    public ProfessionalClient createProfessionalClient(ProfessionalClient client) {
        log.debug("Creating professional client: {}", client.getClientId());
        
        if (clientRepository.existsByClientId(client.getClientId())) {
            throw new DuplicateClientException("Client with ID " + client.getClientId() + " already exists");
        }
        
        ProfessionalClient saved = clientRepository.save(client);
        log.info("Professional client created: {}", saved.getClientId());
        return saved;
    }

    @Transactional(readOnly = true)
    public Client getClientByClientId(String clientId) {
        log.debug("Fetching client: {}", clientId);
        return clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found: " + clientId));
    }

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        log.debug("Fetching all clients");
        return clientRepository.findAll();
    }

    @Transactional
    public void deleteClient(String clientId) {
        log.debug("Deleting client: {}", clientId);
        Client client = getClientByClientId(clientId);
        clientRepository.delete(client);
        log.info("Client deleted: {}", clientId);
    }
}