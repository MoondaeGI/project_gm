package com.example.gitmanager.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "token_seq_gene",
        sequenceName = "TOKEN_ID_SEQ",
        allocationSize = 1
)
@Table(name = "TOKEN")
@Entity
public class Token {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "token_seq_gene")
    private Long id;
    @Column(name = "REFRESH_TOKEN", nullable = false)
    private String token;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;
    @Column(name = "REISSUE_COUNT", nullable = false)
    private Integer reissueCount = 0;

    public void increaseReissueCount() {
        this.reissueCount++;
    }
}
