
package com.example.massagesystem.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String message;
    private String shopName;
    private String role; // role 추가
}
