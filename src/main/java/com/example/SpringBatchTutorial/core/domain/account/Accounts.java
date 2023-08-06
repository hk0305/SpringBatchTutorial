package com.example.SpringBatchTutorial.core.domain.account;

import com.example.SpringBatchTutorial.core.domain.orders.Orders;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@ToString
@Getter
@Entity
@NoArgsConstructor
public class Accounts {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String orderItem;

    private Long price;

    private LocalDate orderDate;

    private LocalDate accountDate;

    public Accounts(Orders orders) {
        this.id = orders.getId();
        this.orderItem = orders.getOrderItem();
        this.price = orders.getPrice();
        this.orderDate = orders.getOrderDate();
        this.accountDate = LocalDate.now();
    }

}
