
package com.example.massagesystem.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "사용자 이름은 필수 입력 값입니다.")
    @Size(min = 4, max = 20, message = "사용자 이름은 4자 이상 20자 이하로 입력해야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "역할은 필수 입력 값입니다.")
    private String role; // e.g., USER, ADMIN

    @NotNull(message = "가게 ID는 필수 입력 값입니다.")
    private Long shopId; // 회원가입 시 선택할 shopId
}
