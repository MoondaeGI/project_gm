package com.example.gitmanager.project.entity;

import com.example.gitmanager.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "project_member_temp_seq_gene",
        sequenceName = "PROJECT_MEMBER_TEMP_ID_SEQ",
        allocationSize = 1
)
@Table(name = "PROJECT_MEMBER_TEMP")
@Entity
public class ProjectMemberTemp {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_member_temp_seq_gene")
    private Long id;
    @CreationTimestamp
    @Column(name = "REG_DATE", nullable = false)
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private Project project;
}
