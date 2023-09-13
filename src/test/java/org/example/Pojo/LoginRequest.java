package org.example.Pojo;

import lombok.Data;

@Data
public class LoginRequest {
    private String userEmail;
    private String userPassword;
}
