
package com.example.massagesystem.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    private String role; // e.g., USER, ADMIN
    private Long shopId; // 회원가입 시 선택할 shopId
}
