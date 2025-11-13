package com.ecommerce.shoppingcart.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing an Individual Client
 * Concrete implementation of Client abstraction
 */
@Entity
@DiscriminatorValue("INDIVIDUAL")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class IndividualClient extends Client {

    @Column(name = "first_name", nullable = true)
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(name = "last_name", nullable = true)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Override
    public boolean isPremiumClient() {
        // Individual clients don't qualify for premium pricing
        return false;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}