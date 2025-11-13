package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.domain.entity.Client;
import com.ecommerce.shoppingcart.domain.entity.IndividualClient;
import com.ecommerce.shoppingcart.domain.entity.ProfessionalClient;
import com.ecommerce.shoppingcart.dto.request.ClientRequest;
import com.ecommerce.shoppingcart.dto.response.Response;
import com.ecommerce.shoppingcart.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Client management
 */
@RestController
@RequestMapping("/v1/clients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Client Management", description = "Client registration and management endpoints")
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/individual")
    @Operation(summary = "Create individual client", description = "Register a new individual client")
    public ResponseEntity<Response.ApiResponse<Response.ClientResponse>> createIndividualClient(
            @Valid @RequestBody ClientRequest.CreateIndividualClientRequest request) {
        
        log.info("Creating individual client: {}", request.getClientId());

        IndividualClient client = IndividualClient.builder()
                .clientId(request.getClientId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        IndividualClient created = clientService.createIndividualClient(client);
        Response.ClientResponse response = mapToClientResponse(created);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.ApiResponse.success("Individual client created successfully", response));
    }

    @PostMapping("/professional")
    @Operation(summary = "Create professional client", description = "Register a new professional/business client")
    public ResponseEntity<Response.ApiResponse<Response.ClientResponse>> createProfessionalClient(
            @Valid @RequestBody ClientRequest.CreateProfessionalClientRequest request) {
        
        log.info("Creating professional client: {}", request.getClientId());

        ProfessionalClient client = ProfessionalClient.builder()
                .clientId(request.getClientId())
                .companyName(request.getCompanyName())
                .vatNumber(request.getVatNumber())
                .businessRegistrationNumber(request.getBusinessRegistrationNumber())
                .annualRevenue(request.getAnnualRevenue())
                .build();

        ProfessionalClient created = clientService.createProfessionalClient(client);
        Response.ClientResponse response = mapToClientResponse(created);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.ApiResponse.success("Professional client created successfully", response));
    }

    @GetMapping("/{clientId}")
    @Operation(summary = "Get client by ID", description = "Retrieve client details by client ID")
    public ResponseEntity<Response.ApiResponse<Response.ClientResponse>> getClient(
            @PathVariable String clientId) {
        
        log.info("Fetching client: {}", clientId);
        Client client = clientService.getClientByClientId(clientId);
        Response.ClientResponse response = mapToClientResponse(client);

        return ResponseEntity.ok(Response.ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Retrieve all registered clients")
    public ResponseEntity<Response.ApiResponse<List<Response.ClientResponse>>> getAllClients() {
        log.info("Fetching all clients");
        List<Client> clients = clientService.getAllClients();
        List<Response.ClientResponse> responses = clients.stream()
                .map(this::mapToClientResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Response.ApiResponse.success(responses));
    }

    @DeleteMapping("/{clientId}")
    @Operation(summary = "Delete client", description = "Delete a client by client ID")
    public ResponseEntity<Response.ApiResponse<Void>> deleteClient(@PathVariable String clientId) {
        log.info("Deleting client: {}", clientId);
        clientService.deleteClient(clientId);
        return ResponseEntity.ok(Response.ApiResponse.success("Client deleted successfully", null));
    }

    private Response.ClientResponse mapToClientResponse(Client client) {
        Response.ClientResponse.ClientResponseBuilder builder = Response.ClientResponse.builder()
                .id(client.getId())
                .clientId(client.getClientId())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt());

        if (client instanceof IndividualClient individual) {
            builder.clientType("INDIVIDUAL")
                    .firstName(individual.getFirstName())
                    .lastName(individual.getLastName())
                    .isPremium(false);
        } else if (client instanceof ProfessionalClient professional) {
            builder.clientType("PROFESSIONAL")
                    .companyName(professional.getCompanyName())
                    .vatNumber(professional.getVatNumber())
                    .businessRegistrationNumber(professional.getBusinessRegistrationNumber())
                    .annualRevenue(professional.getAnnualRevenue())
                    .isPremium(professional.isPremiumClient());
        }

        return builder.build();
    }
}