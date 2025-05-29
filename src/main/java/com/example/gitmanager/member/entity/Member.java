package com.example.gitmanager.member.entity;

import com.example.gitmanager.member.dto.member.MemberUpdateDTO;
import com.example.gitmanager.util.entity.RecordTime;
import com.example.gitmanager.util.enums.ROLE;
import com.example.gitmanager.util.enums.SignInType;
import jakarta.persistence.*;
import lombok.*;

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
    @Enumerated(value = EnumType.STRING)
    @Column(name = "SIGNIN_TYPE", nullable = false)
    private SignInType signInType;
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private ROLE role;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Token token;

    public void update(MemberUpdateDTO dto) {
        this.name = dto.getName();
        this.profileImg = dto.getProfileImg();
    }
}
