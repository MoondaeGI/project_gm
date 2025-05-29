package com.example.gitmanager.member.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenDTO {
    private long id;
    private long memberId;
    private String token;
    private LocalDateTime expirationDate;
}
