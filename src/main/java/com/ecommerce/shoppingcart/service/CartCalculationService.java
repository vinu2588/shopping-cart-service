package com.ecommerce.shoppingcart.service;

import com.ecommerce.shoppingcart.domain.aggregate.ShoppingCart;
import com.ecommerce.shoppingcart.domain.entity.IndividualClient;
import com.ecommerce.shoppingcart.domain.entity.ProfessionalClient;
import com.ecommerce.shoppingcart.domain.enums.ProductType;
import com.ecommerce.shoppingcart.domain.valueobject.CartItem;
import com.ecommerce.shoppingcart.domain.valueobject.CartTotalResult;
import com.ecommerce.shoppingcart.service.factory.PricingStrategyFactory;
import com.ecommerce.shoppingcart.service.strategy.PricingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Service for calculating shopping cart totals
 * Core business logic implementation
 * Following Single Responsibility Principle
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartCalculationService {

    private final PricingStrategyFactory pricingStrategyFactory;

    /**
     * Calculate the total for a shopping cart
     * 
     * @param cart The shopping cart to calculate
     * @return CartTotalResult containing total and breakdown
     */
    public CartTotalResult calculateTotal(ShoppingCart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Shopping cart cannot be null");
        }

        if (!cart.isValid()) {
            throw new IllegalArgumentException("Shopping cart is not valid");
        }

        String clientId = cart.getClient().getClientId();
        log.debug("Calculating total for client: {}", clientId);

        PricingStrategy strategy = pricingStrategyFactory.getStrategy(cart.getClient());
        
        Map<ProductType, CartTotalResult.LineItemDetail> lineItems = new LinkedHashMap<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            BigDecimal unitPrice = strategy.getPrice(item.getProductType());
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);

            CartTotalResult.LineItemDetail detail = CartTotalResult.LineItemDetail.builder()
                    .productType(item.getProductType())
                    .quantity(item.getQuantity())
                    .unitPrice(unitPrice)
                    .lineTotal(lineTotal)
                    .build();

            lineItems.merge(item.getProductType(), detail, (existing, newDetail) ->
                    CartTotalResult.LineItemDetail.builder()
                            .productType(existing.getProductType())
                            .quantity(existing.getQuantity() + newDetail.getQuantity())
                            .unitPrice(existing.getUnitPrice())
                            .lineTotal(existing.getLineTotal().add(newDetail.getLineTotal()))
                            .build()
            );

            total = total.add(lineTotal);
        }

        String clientName = getClientName(cart);
        String clientType = cart.getClient() instanceof IndividualClient ? "INDIVIDUAL" : "PROFESSIONAL";

        log.info("Cart total calculated: {} for client: {}", total, cart.getClient().getClientId());

        return CartTotalResult.builder()
                .clientId(cart.getClient().getClientId())
                .clientType(clientType)
                .clientName(clientName)
                .totalAmount(total.setScale(2, RoundingMode.HALF_UP))
                .totalItems(cart.getTotalItemCount())
                .lineItems(lineItems)
                .build();
    }

    private String getClientName(ShoppingCart cart) {
        if (cart.getClient() instanceof IndividualClient individual) {
            return individual.getFullName();
        } else if (cart.getClient() instanceof ProfessionalClient professional) {
            return professional.getCompanyName();
        }
        return "Unknown";
    }
}