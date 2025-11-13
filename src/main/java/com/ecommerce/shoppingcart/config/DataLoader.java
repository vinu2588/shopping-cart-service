package com.ecommerce.shoppingcart.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecommerce.shoppingcart.domain.entity.IndividualClient;
import com.ecommerce.shoppingcart.domain.entity.ProfessionalClient;
import com.ecommerce.shoppingcart.repository.ClientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Data loader for initializing sample data
 * Useful for demos and testing
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(ClientRepository clientRepository) {
        return args -> {
            log.info("Loading sample data...");

            // Individual Clients
            IndividualClient john = IndividualClient.builder()
                    .clientId("IND001")
                    .firstName("John")
                    .lastName("Doe")
                    .build();

            IndividualClient jane = IndividualClient.builder()
                    .clientId("IND002")
                    .firstName("Jane")
                    .lastName("Smith")
                    .build();

            // Professional Clients - High Revenue (Premium)
            ProfessionalClient techCorp = ProfessionalClient.builder()
                    .clientId("PRO001")
                    .companyName("TechCorp International")
                    .vatNumber("FR12345678901")
                    .businessRegistrationNumber("RCS-PARIS-123456")
                    .annualRevenue(new BigDecimal("15000000.00"))
                    .build();

            // Professional Clients - Low Revenue (Standard)
            ProfessionalClient startupLtd = ProfessionalClient.builder()
                    .clientId("PRO002")
                    .companyName("Startup Ltd")
                    .businessRegistrationNumber("RCS-LYON-789012")
                    .annualRevenue(new BigDecimal("5000000.00"))
                    .build();

            ProfessionalClient smallBiz = ProfessionalClient.builder()
                    .clientId("PRO003")
                    .companyName("Small Business GmbH")
                    .vatNumber("DE987654321")
                    .businessRegistrationNumber("HRB-12345")
                    .annualRevenue(new BigDecimal("2500000.00"))
                    .build();

            clientRepository.save(john);
            clientRepository.save(jane);
            clientRepository.save(techCorp);
            clientRepository.save(startupLtd);
            clientRepository.save(smallBiz);

            log.info("Sample data loaded successfully:");
            log.info("  - Individual Clients: IND001, IND002");
            log.info("  - Professional Clients (Premium): PRO001");
            log.info("  - Professional Clients (Standard): PRO002, PRO003");
            log.info("API is ready for demo at http://localhost:8080/api");
            log.info("Swagger UI: http://localhost:8080/api/swagger-ui.html");
        };
    }
}