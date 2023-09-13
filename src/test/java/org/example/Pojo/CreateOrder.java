package org.example.Pojo;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrder {
    private List<OrderList> orders;
}
