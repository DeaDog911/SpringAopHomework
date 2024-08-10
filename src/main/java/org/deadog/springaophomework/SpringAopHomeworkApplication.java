package org.deadog.springaophomework;

import org.deadog.springaophomework.model.Order;
import org.deadog.springaophomework.model.User;
import org.deadog.springaophomework.service.OrderService;
import org.deadog.springaophomework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SpringAopHomeworkApplication {
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(SpringAopHomeworkApplication.class, args);
    }
}
