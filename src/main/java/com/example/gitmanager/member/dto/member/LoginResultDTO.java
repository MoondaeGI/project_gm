package com.example.gitmanager.member.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResultDTO {
    private long id;
    private String role;
    private String token;
    private String refreshToken;
}
