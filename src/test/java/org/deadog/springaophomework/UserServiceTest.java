package org.deadog.springaophomework;

import org.deadog.springaophomework.aspect.LoggingAspect;
import org.deadog.springaophomework.model.Order;
import org.deadog.springaophomework.model.User;
import org.deadog.springaophomework.repository.OrderRepository;
import org.deadog.springaophomework.repository.UserRepository;
import org.deadog.springaophomework.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.DefaultAopProxyFactory;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private LoggingAspect aspect = new LoggingAspect();

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory(userService);
        aspectJProxyFactory.addAspect(aspect);

        DefaultAopProxyFactory proxyFactory = new DefaultAopProxyFactory();
        AopProxy aopProxy = proxyFactory.createAopProxy(aspectJProxyFactory);

        userService = (UserService) aopProxy.getProxy();
    }

    @Test
    void createUser_shouldCreateUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    void getUserWithOrders_shouldReturnUserWithOrders_whenUserExists() {
        User user = new User();
        Order order = new Order();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.findAllByUserId(1L)).thenReturn(Collections.singletonList(order));

        User foundUser = userService.getUserWithOrders(1L);

        assertNotNull(foundUser);
        assertEquals(1, foundUser.getOrders().size());
    }

    @Test
    void updateUser_shouldUpdateUser_whenUserExists() {
        User user = new User();
        User updatedDetails = new User();
        updatedDetails.setName("New Name");
        updatedDetails.setEmail("new.email@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.updateUser(1L, updatedDetails);

        assertNotNull(updatedUser);
        assertEquals("New Name", updatedUser.getName());
        assertEquals("new.email@example.com", updatedUser.getEmail());
    }

    @Test
    void deleteUser_shouldDeleteUser_whenUserExists() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
