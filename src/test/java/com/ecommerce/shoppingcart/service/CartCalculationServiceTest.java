package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.config.PricingConfiguration;
import com.ecommerce.shoppingcart.domain.aggregate.ShoppingCart;
import com.ecommerce.shoppingcart.domain.entity.IndividualClient;
import com.ecommerce.shoppingcart.domain.entity.ProfessionalClient;
import com.ecommerce.shoppingcart.domain.enums.ProductType;
import com.ecommerce.shoppingcart.domain.valueobject.CartItem;
import com.ecommerce.shoppingcart.domain.valueobject.CartTotalResult;
import com.ecommerce.shoppingcart.service.factory.PricingStrategyFactory;
import com.ecommerce.shoppingcart.service.strategy.IndividualClientPricingStrategy;
import com.ecommerce.shoppingcart.service.strategy.ProfessionalHighRevenuePricingStrategy;
import com.ecommerce.shoppingcart.service.strategy.ProfessionalLowRevenuePricingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for CartCalculationService
 * Following TDD best practices
 */
@ExtendWith(MockitoExtension.class)
class CartCalculationServiceTest {

    private CartCalculationService cartCalculationService;
    private PricingConfiguration pricingConfig;

    @BeforeEach
    void setUp() {
        pricingConfig = new PricingConfiguration();
        
        // Initialize pricing strategies
        IndividualClientPricingStrategy individualStrategy = 
                new IndividualClientPricingStrategy(pricingConfig);
        ProfessionalHighRevenuePricingStrategy highRevenueStrategy = 
                new ProfessionalHighRevenuePricingStrategy(pricingConfig);
        ProfessionalLowRevenuePricingStrategy lowRevenueStrategy = 
                new ProfessionalLowRevenuePricingStrategy(pricingConfig);
        
        PricingStrategyFactory factory = new PricingStrategyFactory(
                List.of(individualStrategy, highRevenueStrategy, lowRevenueStrategy));
        
        cartCalculationService = new CartCalculationService(factory);
    }

    @Test
    @DisplayName("Should calculate correct total for individual client with single high-end phone")
    void testIndividualClientSingleHighEndPhone() {
        // Given
        IndividualClient client = IndividualClient.builder()
                .clientId("IND001")
                .firstName("John")
                .lastName("Doe")
                .build();

        ShoppingCart cart = ShoppingCart.builder()
                .client(client)
                .build();
        cart.addItem(CartItem.of(ProductType.HIGH_END_PHONE, 1));

        // When
        CartTotalResult result = cartCalculationService.calculateTotal(cart);

        // Then
        assertEquals(new BigDecimal("1500.00"), result.getTotalAmount());
        assertEquals(1, result.getTotalItems());
    }

    @Test
    @DisplayName("Should calculate correct total for individual client with multiple items")
    void testIndividualClientMultipleItems() {
        // Given
        IndividualClient client = IndividualClient.builder()
                .clientId("IND001")
                .firstName("John")
                .lastName("Doe")
                .build();

        ShoppingCart cart = ShoppingCart.builder()
                .client(client)
                .build();
        cart.addItem(CartItem.of(ProductType.HIGH_END_PHONE, 2));
        cart.addItem(CartItem.of(ProductType.MID_RANGE_PHONE, 1));
        cart.addItem(CartItem.of(ProductType.LAPTOP, 1));

        // When
        CartTotalResult result = cartCalculationService.calculateTotal(cart);

        // Then
        // 2 * 1500 + 1 * 800 + 1 * 1200 = 5000
        assertEquals(new BigDecimal("5000.00"), result.getTotalAmount());
        assertEquals(4, result.getTotalItems());
    }

    @Test
    @DisplayName("Should calculate correct total for professional high revenue client")
    void testProfessionalHighRevenueClient() {
        // Given
        ProfessionalClient client = ProfessionalClient.builder()
                .clientId("PRO001")
                .companyName("TechCorp")
                .businessRegistrationNumber("REG123")
                .annualRevenue(new BigDecimal("15000000.00"))
                .build();

        ShoppingCart cart = ShoppingCart.builder()
                .client(client)
                .build();
        cart.addItem(CartItem.of(ProductType.HIGH_END_PHONE, 1));
        cart.addItem(CartItem.of(ProductType.MID_RANGE_PHONE, 1));
        cart.addItem(CartItem.of(ProductType.LAPTOP, 1));

        // When
        CartTotalResult result = cartCalculationService.calculateTotal(cart);

        // Then
        // 1 * 1000 + 1 * 550 + 1 * 900 = 2450
        assertEquals(new BigDecimal("2450.00"), result.getTotalAmount());
        assertEquals(3, result.getTotalItems());
    }

    @Test
    @DisplayName("Should calculate correct total for professional low revenue client")
    void testProfessionalLowRevenueClient() {
        // Given
        ProfessionalClient client = ProfessionalClient.builder()
                .clientId("PRO002")
                .companyName("Startup Ltd")
                .businessRegistrationNumber("REG456")
                .annualRevenue(new BigDecimal("5000000.00"))
                .build();

        ShoppingCart cart = ShoppingCart.builder()
                .client(client)
                .build();
        cart.addItem(CartItem.of(ProductType.HIGH_END_PHONE, 1));
        cart.addItem(CartItem.of(ProductType.MID_RANGE_PHONE, 1));
        cart.addItem(CartItem.of(ProductType.LAPTOP, 1));

        // When
        CartTotalResult result = cartCalculationService.calculateTotal(cart);

        // Then
        // 1 * 1150 + 1 * 600 + 1 * 1000 = 2750
        assertEquals(new BigDecimal("2750.00"), result.getTotalAmount());
        assertEquals(3, result.getTotalItems());
    }

    @Test
    @DisplayName("Should throw exception for invalid cart")
    void testInvalidCart() {
        // Given
        ShoppingCart cart = ShoppingCart.builder().build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
                cartCalculationService.calculateTotal(cart));
    }

    @Test
    @DisplayName("Should correctly identify premium client status")
    void testPremiumClientIdentification() {
        // High revenue client - should be premium
        ProfessionalClient highRevenue = ProfessionalClient.builder()
                .clientId("PRO001")
                .companyName("BigCorp")
                .businessRegistrationNumber("REG123")
                .annualRevenue(new BigDecimal("15000000.00"))
                .build();
        assertTrue(highRevenue.isPremiumClient());

        // Low revenue client - should not be premium
        ProfessionalClient lowRevenue = ProfessionalClient.builder()
                .clientId("PRO002")
                .companyName("SmallCorp")
                .businessRegistrationNumber("REG456")
                .annualRevenue(new BigDecimal("5000000.00"))
                .build();
        assertFalse(lowRevenue.isPremiumClient());

        // Individual client - should not be premium
        IndividualClient individual = IndividualClient.builder()
                .clientId("IND001")
                .firstName("John")
                .lastName("Doe")
                .build();
        assertFalse(individual.isPremiumClient());
    }
}