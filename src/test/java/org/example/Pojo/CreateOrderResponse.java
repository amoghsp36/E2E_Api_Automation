package org.example.Pojo;

import lombok.Data;

import java.util.List;
@Data
public class CreateOrderResponse {
    private List<String> orders;
    private List<String> productOrderId;
    private String message;
}
