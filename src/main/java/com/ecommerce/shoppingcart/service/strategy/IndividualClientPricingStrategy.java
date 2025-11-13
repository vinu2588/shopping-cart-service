package com.ecommerce.shoppingcart.service.strategy;

import com.ecommerce.shoppingcart.config.PricingConfiguration;
import com.ecommerce.shoppingcart.domain.entity.Client;
import com.ecommerce.shoppingcart.domain.entity.IndividualClient;
import com.ecommerce.shoppingcart.domain.enums.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Pricing strategy for Individual Clients
 * Concrete implementation of Strategy pattern
 */
@Component
@RequiredArgsConstructor
public class IndividualClientPricingStrategy implements PricingStrategy {

    private final PricingConfiguration pricingConfig;

    @Override
    public BigDecimal getPrice(ProductType productType) {
        return switch (productType) {
            case HIGH_END_PHONE -> pricingConfig.getIndividual().getHighEndPhone();
            case MID_RANGE_PHONE -> pricingConfig.getIndividual().getMidRangePhone();
            case LAPTOP -> pricingConfig.getIndividual().getLaptop();
        };
    }

    @Override
    public boolean appliesTo(Client client) {
        return client instanceof IndividualClient;
    }
}