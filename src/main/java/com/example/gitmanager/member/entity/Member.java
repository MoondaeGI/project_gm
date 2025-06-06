package com.example.gitmanager.member.entity;

import com.example.gitmanager.member.dto.member.MemberUpdateDTO;
import com.example.gitmanager.notice.entity.NoticeReply;
import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.util.entity.RecordTime;
import com.example.gitmanager.util.enums.ROLE;
import com.example.gitmanager.util.enums.SignInType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "member_seq_gene",
        sequenceName = "MEMBER_ID_SEQ",
        allocationSize = 1
)
@Table(name = "MEMEBER")
@Entity
public class Member extends RecordTime {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "member_seq_gene")
    private Long id;
    @Column(name = "NAME", length = 100, nullable = false)
    private String name;
    @Column(name = "EMAIL", length = 100, nullable = false, unique = true)
    private String email;
    @Column(name = "LOGIN_ID", length = 100, unique = true)
    private String loginId;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "PROFILE_IMG", length = 300, nullable = false)
    private String profileImg;
    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    @Column(name = "SIGNIN_TYPE", nullable = false)
    private SignInType signInType = SignInType.NONE;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "ROLE", nullable = false)
    private ROLE role = ROLE.USER;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Token token;

    @OneToMany(mappedBy = "writer")
    private List<NoticeReply> noticeReplyList;
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProjectMember> projectMemberList;

    public void update(MemberUpdateDTO dto) {
        this.name = dto.getName();
        this.profileImg = dto.getProfileImg();
    }
}
