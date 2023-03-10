package com.ourlife.dto.user;

import lombok.*;

import java.time.LocalDate;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateUserRequest {

    private String nickname;

    private String email;

    private String birth;

    private String introduce;
}
