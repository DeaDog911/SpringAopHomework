package org.deadog.springaophomework;

import org.deadog.springaophomework.aspect.LoggingAspect;
import org.deadog.springaophomework.exceptions.ApplicationException;
import org.deadog.springaophomework.model.Order;
import org.deadog.springaophomework.model.User;
import org.deadog.springaophomework.repository.OrderRepository;
import org.deadog.springaophomework.repository.UserRepository;
import org.deadog.springaophomework.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.DefaultAopProxyFactory;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    private LoggingAspect aspect = new LoggingAspect();

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory(orderService);
        aspectJProxyFactory.addAspect(aspect);

        DefaultAopProxyFactory proxyFactory = new DefaultAopProxyFactory();
        AopProxy aopProxy = proxyFactory.createAopProxy(aspectJProxyFactory);

        orderService = (OrderService) aopProxy.getProxy();
    }

    @Test
    void create_shouldCreateOrder_whenUserExists() {
        User user = new User();
        user.setId(1L);

        Order order = new Order();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.save(order)).thenReturn(order);

        Order createdOrder = orderService.create(1L, order);

        assertNotNull(createdOrder);
        assertEquals(user, createdOrder.getUser());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void create_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> orderService.create(1L, new Order()));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() {
        Order order = new Order();
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        assertEquals(1, orderService.getAllOrders().size());
    }

    @Test
    void getOrderById_shouldReturnOrder_whenOrderExists() {
        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(1L);

        assertNotNull(foundOrder);
        assertEquals(order, foundOrder);
    }

    @Test
    void updateOrder_shouldUpdateOrder_whenOrderExists() {
        Order order = new Order();
        Order updatedDetails = new Order();
        updatedDetails.setDescription("New Description");
        updatedDetails.setStatus("Updated Status");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order updatedOrder = orderService.updateOrder(1L, updatedDetails);

        assertNotNull(updatedOrder);
        assertEquals("New Description", updatedOrder.getDescription());
        assertEquals("Updated Status", updatedOrder.getStatus());
    }

    @Test
    void deleteOrder_shouldDeleteOrder_whenOrderExists() {
        orderService.deleteOrder(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }
}
