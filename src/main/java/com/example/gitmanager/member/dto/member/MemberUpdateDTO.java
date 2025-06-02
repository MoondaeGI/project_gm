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
public class MemberUpdateDTO {
    private long id;
    private String name;
    private String profileImg;
    private MultipartFile multipartFile;
}
