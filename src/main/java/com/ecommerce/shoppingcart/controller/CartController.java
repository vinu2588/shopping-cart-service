package com.ecommerce.shoppingcart.controller;

import com.ecommerce.shoppingcart.domain.aggregate.ShoppingCart;
import com.ecommerce.shoppingcart.domain.entity.Client;
import com.ecommerce.shoppingcart.domain.valueobject.CartItem;
import com.ecommerce.shoppingcart.domain.valueobject.CartTotalResult;
import com.ecommerce.shoppingcart.dto.request.CartCalculationRequest;
import com.ecommerce.shoppingcart.dto.response.Response;
import com.ecommerce.shoppingcart.service.CartCalculationService;
import com.ecommerce.shoppingcart.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Shopping Cart operations
 * Following RESTful principles and best practices
 */
@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Shopping Cart", description = "Shopping cart calculation endpoints")
public class CartController {

    private final CartCalculationService cartCalculationService;
    private final ClientService clientService;

    @PostMapping("/calculate")
    @Operation(summary = "Calculate cart total", 
               description = "Calculate the total cost for a shopping cart based on client type and pricing rules")
    public ResponseEntity<Response.ApiResponse<Response.CartTotalResponse>> calculateCartTotal(
            @Valid @RequestBody CartCalculationRequest request) {
        
        log.info("Received cart calculation request for client: {}", request.getClientId());

        // Fetch client
        Client client = clientService.getClientByClientId(request.getClientId());

        // Build shopping cart
        ShoppingCart cart = ShoppingCart.builder()
                .client(client)
                .build();

        // Add items to cart
        request.getItems().forEach(itemDto -> {
            CartItem item = CartItem.of(itemDto.getProductType(), itemDto.getQuantity());
            cart.addItem(item);
        });

        // Calculate total
        CartTotalResult result = cartCalculationService.calculateTotal(cart);

        // Map to response DTO
        Response.CartTotalResponse response = mapToResponse(result);

        log.info("Cart calculation completed for client: {}, Total: €{}", 
                 request.getClientId(), result.getTotalAmount());

        return ResponseEntity.ok(Response.ApiResponse.success(
                "Cart total calculated successfully", response));
    }

    private Response.CartTotalResponse mapToResponse(CartTotalResult result) {
        List<Response.LineItemResponse> lineItems = result.getLineItems().values().stream()
                .map(detail -> Response.LineItemResponse.builder()
                        .productType(detail.getProductType())
                        .productName(detail.getProductType().getDisplayName())
                        .quantity(detail.getQuantity())
                        .unitPrice(detail.getUnitPrice())
                        .lineTotal(detail.getLineTotal())
                        .build())
                .collect(Collectors.toList());

        return Response.CartTotalResponse.builder()
                .clientId(result.getClientId())
                .clientType(result.getClientType())
                .clientName(result.getClientName())
                .totalAmount(result.getTotalAmount())
                .currency("EUR")
                .totalItems(result.getTotalItems())
                .lineItems(lineItems)
                .calculatedAt(LocalDateTime.now())
                .build();
    }
}