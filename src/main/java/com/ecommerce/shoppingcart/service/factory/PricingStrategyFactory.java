package com.ecommerce.shoppingcart.service.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ecommerce.shoppingcart.domain.entity.Client;
import com.ecommerce.shoppingcart.exception.PricingStrategyNotFoundException;
import com.ecommerce.shoppingcart.service.strategy.PricingStrategy;

import lombok.RequiredArgsConstructor;

/**
 * Factory for selecting appropriate pricing strategy
 * Implements Factory pattern with Spring's dependency injection
 * All strategies are auto-wired through constructor injection
 */
@Component
@RequiredArgsConstructor
public class PricingStrategyFactory {

    private final List<PricingStrategy> pricingStrategies;

    /**
     * Select the appropriate pricing strategy for a given client
     * 
     * @param client The client for whom to select a strategy
     * @return The appropriate pricing strategy
     * @throws PricingStrategyNotFoundException if no strategy is found
     */
    public PricingStrategy getStrategy(Client client) {
        return pricingStrategies.stream()
                .filter(strategy -> strategy.appliesTo(client))
                .findFirst()
                .orElseThrow(() -> new PricingStrategyNotFoundException(
                        "No pricing strategy found for client type: " + 
                        client.getClass().getSimpleName()
                ));
    }
}