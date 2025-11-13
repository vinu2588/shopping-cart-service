package com.ecommerce.shoppingcart.repository;

import com.ecommerce.shoppingcart.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Client entity
 * Following Repository pattern with Spring Data JPA
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Find client by clientId
     * 
     * @param clientId The unique client identifier
     * @return Optional containing the client if found
     */
    Optional<Client> findByClientId(String clientId);

    /**
     * Check if client exists by clientId
     * 
     * @param clientId The unique client identifier
     * @return true if client exists
     */
    boolean existsByClientId(String clientId);
}