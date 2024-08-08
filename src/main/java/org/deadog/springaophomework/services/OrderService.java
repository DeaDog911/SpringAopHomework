package org.deadog.springaophomework.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.deadog.springaophomework.annotations.Logging;
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
public class OrderService {
    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    public Order create(Long userId, Order order) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null)
            throw new ApplicationException("User not found");

        order.setUser(user);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setDescription(orderDetails.getDescription());
            order.setStatus(orderDetails.getStatus());
            return orderRepository.save(order);
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

}
