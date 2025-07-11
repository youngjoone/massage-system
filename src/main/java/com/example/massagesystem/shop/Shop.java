
package com.example.massagesystem.shop;

import com.example.massagesystem.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Shop extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "가게 이름은 필수 입력 값입니다.")
    @Size(max = 100, message = "가게 이름은 100자를 초과할 수 없습니다.")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    @Size(max = 255, message = "주소는 255자를 초과할 수 없습니다.")
    @Column(nullable = false)
    private String address;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Size(max = 20, message = "전화번호는 20자를 초과할 수 없습니다.")
    @Column(nullable = false)
    private String phoneNumber;
}
