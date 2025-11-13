package com.ecommerce.shoppingcart.service.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.ecommerce.shoppingcart.config.PricingConfiguration;
import com.ecommerce.shoppingcart.domain.entity.Client;
import com.ecommerce.shoppingcart.domain.entity.ProfessionalClient;
import com.ecommerce.shoppingcart.domain.enums.ProductType;

import lombok.RequiredArgsConstructor;

/**
 * Pricing strategy for Professional Clients with high revenue (> €10M)
 * Premium pricing tier
 */
@Component
@RequiredArgsConstructor
public class ProfessionalHighRevenuePricingStrategy implements PricingStrategy {

    private final PricingConfiguration pricingConfig;

    @Override
    public BigDecimal getPrice(ProductType productType) {
        return switch (productType) {
            case HIGH_END_PHONE -> pricingConfig.getProfessional().getHighRevenue().getHighEndPhone();
            case MID_RANGE_PHONE -> pricingConfig.getProfessional().getHighRevenue().getMidRangePhone();
            case LAPTOP -> pricingConfig.getProfessional().getHighRevenue().getLaptop();
        };
    }

    @Override
    public boolean appliesTo(Client client) {
        if (!(client instanceof ProfessionalClient professionalClient)) {
            return false;
        }
        return professionalClient.isPremiumClient();
    }
}