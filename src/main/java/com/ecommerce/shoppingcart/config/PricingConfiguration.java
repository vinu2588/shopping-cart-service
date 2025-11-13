package com.ecommerce.shoppingcart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Configuration Properties for Pricing
 * Externalized configuration following 12-factor app principles
 * Allows for easy price changes without code modification
 */
@Component
@ConfigurationProperties(prefix = "pricing")
@Data
public class PricingConfiguration {

    private IndividualPricing individual = new IndividualPricing();
    private ProfessionalPricing professional = new ProfessionalPricing();

    @Data
    public static class IndividualPricing {
        private BigDecimal highEndPhone = new BigDecimal("1500.00");
        private BigDecimal midRangePhone = new BigDecimal("800.00");
        private BigDecimal laptop = new BigDecimal("1200.00");
    }

    @Data
    public static class ProfessionalPricing {
        private BigDecimal highRevenueThreshold = new BigDecimal("10000000.00");
        private HighRevenuePricing highRevenue = new HighRevenuePricing();
        private LowRevenuePricing lowRevenue = new LowRevenuePricing();
    }

    @Data
    public static class HighRevenuePricing {
        private BigDecimal highEndPhone = new BigDecimal("1000.00");
        private BigDecimal midRangePhone = new BigDecimal("550.00");
        private BigDecimal laptop = new BigDecimal("900.00");
    }

    @Data
    public static class LowRevenuePricing {
        private BigDecimal highEndPhone = new BigDecimal("1150.00");
        private BigDecimal midRangePhone = new BigDecimal("600.00");
        private BigDecimal laptop = new BigDecimal("1000.00");
    }
}