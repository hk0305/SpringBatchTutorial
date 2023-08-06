package com.example.SpringBatchTutorial.core.domain.orders;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@ToString
@Getter
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String orderItem;

    private Long price;

    private LocalDate orderDate;

}
