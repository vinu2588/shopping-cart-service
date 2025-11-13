package com.ecommerce.shoppingcart.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Entity representing a Professional/Business Client
 * Implements business rules for premium client classification
 */
@Entity
@DiscriminatorValue("PROFESSIONAL")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalClient extends Client {

    @Column(name = "company_name", nullable = true)
    @NotBlank(message = "Company name is required")
    private String companyName;

    @Column(name = "vat_number")
    private String vatNumber; // Optional intra-community VAT number

    @Column(name = "business_registration_number", nullable = true)
    @NotBlank(message = "Business registration number is required")
    private String businessRegistrationNumber;

    @Column(name = "annual_revenue", nullable = true, precision = 15, scale = 2)
    @NotNull(message = "Annual revenue is required")
    @PositiveOrZero(message = "Annual revenue must be positive or zero")
    private BigDecimal annualRevenue;

    private static final BigDecimal PREMIUM_REVENUE_THRESHOLD = new BigDecimal("10000000");

    @Override
    public boolean isPremiumClient() {
        // Professional clients with revenue > €10M qualify for premium pricing
        return annualRevenue != null && 
               annualRevenue.compareTo(PREMIUM_REVENUE_THRESHOLD) > 0;
    }

    public boolean hasVatNumber() {
        return vatNumber != null && !vatNumber.trim().isEmpty();
    }
}