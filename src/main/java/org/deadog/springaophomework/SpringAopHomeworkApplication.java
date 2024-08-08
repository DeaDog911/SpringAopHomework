package org.deadog.springaophomework;

import org.aspectj.weaver.ast.Or;
import org.deadog.springaophomework.model.Order;
import org.deadog.springaophomework.model.User;
import org.deadog.springaophomework.services.OrderService;
import org.deadog.springaophomework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication
public class SpringAopHomeworkApplication {
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(SpringAopHomeworkApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent(ApplicationReadyEvent e) {
        OrderService orderService = applicationContext.getBean(OrderService.class);
        UserService userService = applicationContext.getBean(UserService.class);
//        User user = new User();
//        user.setEmail("2222@mail.ru");
//        user.setName("test");
//        user = userService.createUser(user);
//
//        Order order = new Order();
//        order.setDescription("test");
//        order.setUser(user);
//        orderService.create(user.getId(), order);

        User user = userService.getUserWithOrders(1L);
        for (Order order : user.getOrders()) {
            System.out.println(order.getDescription());
        }
        userService.voidMethod();
    }

}
