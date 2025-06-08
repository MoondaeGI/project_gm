package com.example.gitmanager.board.entity.notice;

import com.example.gitmanager.project.entity.ProjectMember;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "project_notice_view_seq_gene",
        sequenceName = "PROJECT_NOTICE_VIEW_ID_SEQ",
        allocationSize = 1
)
@Table(name = "PROJECT_NOTICE_VIEW")
@Entity
public class ProjectNoticeView {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_notice_view_seq_gene")
    private Long id;
    @CreationTimestamp
    @Column(name = "REG_DATE", nullable = false)
    private String regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_NOTICE_ID", nullable = false)
    private ProjectNotice projectNotice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_MEMBER_ID", nullable = false)
    private ProjectMember projectMember;
}
