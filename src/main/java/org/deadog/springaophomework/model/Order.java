package org.deadog.springaophomework.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String status;

    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
