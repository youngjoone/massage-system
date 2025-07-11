package com.example.massagesystem.announcement;

import com.example.massagesystem.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Announcement extends BaseEntity {

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

    // 더미 데이터 생성을 위한 생성자
    public Announcement(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}