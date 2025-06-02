package com.example.gitmanager.member.dto.member;

import com.example.gitmanager.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    private long id;
    private String name;
    private String email;
    private String profileImg;

    public static MemberDTO of(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .profileImg(member.getProfileImg())
                .build();
    }
}
