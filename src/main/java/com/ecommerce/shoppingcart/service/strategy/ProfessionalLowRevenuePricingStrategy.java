package com.ecommerce.shoppingcart.service.strategy;

import com.ecommerce.shoppingcart.config.PricingConfiguration;
import com.ecommerce.shoppingcart.domain.entity.Client;
import com.ecommerce.shoppingcart.domain.entity.ProfessionalClient;
import com.ecommerce.shoppingcart.domain.enums.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Pricing strategy for Professional Clients with low revenue (< €10M)
 * Standard professional pricing tier
 */
@Component
@RequiredArgsConstructor
public class ProfessionalLowRevenuePricingStrategy implements PricingStrategy {

    private final PricingConfiguration pricingConfig;

    @Override
    public BigDecimal getPrice(ProductType productType) {
        return switch (productType) {
            case HIGH_END_PHONE -> pricingConfig.getProfessional().getLowRevenue().getHighEndPhone();
            case MID_RANGE_PHONE -> pricingConfig.getProfessional().getLowRevenue().getMidRangePhone();
            case LAPTOP -> pricingConfig.getProfessional().getLowRevenue().getLaptop();
        };
    }

    @Override
    public boolean appliesTo(Client client) {
        if (!(client instanceof ProfessionalClient professionalClient)) {
            return false;
        }
        return !professionalClient.isPremiumClient();
    }
}