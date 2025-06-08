package com.example.gitmanager.project.entity;

import com.example.gitmanager.board.entity.board.Board;
import com.example.gitmanager.board.entity.board.Reply;
import com.example.gitmanager.board.entity.notice.ProjectNoticeView;
import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.util.enums.Yn;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "project_member_seq_gene",
        sequenceName = "PROJECT_MEMBER_ID_SEQ",
        allocationSize = 1
)
@Table(name = "PROJECT_MEMBER")
@Entity
public class ProjectMember {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_member_seq_gene")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "LEADER_YN", nullable = false)
    private Yn leaderYn;
    @CreationTimestamp
    @Column(name = "JOIN_DATE", nullable = false)
    private LocalDateTime joinDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Board> boardList;
    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reply> replyList;
    @OneToMany(mappedBy = "projectMember", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProjectNoticeView> projectNoticeViewList;

    public void toggleLeaderYn() {
        this.leaderYn = this.leaderYn == Yn.Y ? Yn.N : Yn.Y;
    }
}
