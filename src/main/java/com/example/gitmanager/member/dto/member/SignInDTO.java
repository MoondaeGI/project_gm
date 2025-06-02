package com.example.gitmanager.member.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInDTO {
    private String name;
    private String loginId;
    private String password;
    private String email;
    private String profileImg;
    private MultipartFile multipartFile;
}
