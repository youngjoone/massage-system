package com.example.massagesystem.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.massagesystem.shop.Shop; // Shop 임포트

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users") // 'user' is a reserved keyword in some databases
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "사용자 이름은 필수 입력 값입니다.")
    @Size(min = 4, max = 20, message = "사용자 이름은 4자 이상 20자 이하로 입력해야 합니다.")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "역할은 필수 입력 값입니다.")
    @Column(nullable = false)
    private String role; // e.g., ADMIN, USER

    @ManyToOne // User와 Shop은 Many-to-One 관계
    @JoinColumn(name = "shop_id", nullable = false) // shop_id 컬럼으로 매핑
    private Shop shop;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
