package com.empresa.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.empresa.api.model.dto.OrderRequestDTO;
import com.empresa.api.model.entity.Order;
import com.empresa.api.repository.OrderRepository;
import com.empresa.api.service.OrderService;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
	@Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCreateOrder_thenOrderIsSaved() {
        // Given
        OrderRequestDTO dto = new OrderRequestDTO("CDMX", "Michoacán");
        Order savedOrder = new Order();
        savedOrder.setId(UUID.randomUUID());
        savedOrder.setOrigin("CDMX");

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // When
        Order result = orderService.createOrder(dto);

        // Then
        assertNotNull(result.getId());
        assertEquals("CDMX", result.getOrigin());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
