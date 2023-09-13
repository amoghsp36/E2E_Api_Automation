package org.example.Pojo;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String userId;
    private String message;
}
