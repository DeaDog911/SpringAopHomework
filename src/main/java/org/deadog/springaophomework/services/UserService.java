package org.deadog.springaophomework.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.deadog.springaophomework.annotations.Logging;
import org.deadog.springaophomework.annotations.Timer;
import org.deadog.springaophomework.exceptions.ApplicationException;
import org.deadog.springaophomework.model.Order;
import org.deadog.springaophomework.model.User;
import org.deadog.springaophomework.repository.OrderRepository;
import org.deadog.springaophomework.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Logging
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Timer
    @Transactional
    public User getUserWithOrders(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return null;

        List<Order> orders = orderRepository.findAllByUserId(id);
        user.setOrders(orders);
        return user;
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Timer
    public void voidMethod() {}
}
