package com.example.massagesystem.announcement;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다.")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(max = 2000, message = "내용은 2000자를 초과할 수 없습니다.")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
