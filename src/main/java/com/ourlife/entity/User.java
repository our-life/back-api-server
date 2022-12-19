package com.ourlife.entity;

import com.ourlife.dto.user.UpdateUserRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "users")
@Entity
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String password;

    private String nickname;

    @Column(unique = true)
    private String email;

    private LocalDate birth;

    private String introduce;

    private String profileImgUrl;

    public void update(UpdateUserRequest request) {
        this.nickname = request.getNickname();
        this.email = request.getEmail();
        this.birth = request.getBirth();
        this.introduce = request.getIntroduce();
    }
}
