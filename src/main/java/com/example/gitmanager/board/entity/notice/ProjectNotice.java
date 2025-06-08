package com.example.gitmanager.board.entity.notice;

import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.util.entity.RecordTime;
import com.example.gitmanager.util.enums.Yn;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "project_notice_seq_gene",
        sequenceName = "PROJECT_NOTICE_ID_SEQ",
        allocationSize = 1
)
@Table(name = "PROJECT_NOTICE")
@Entity
public class ProjectNotice extends RecordTime {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_notice_seq_gene")
    private Long id;
    @Column(name = "TITLE", length = 300, nullable = false)
    private String title;
    @Column(name = "CONTENT", columnDefinition = "CLOB", nullable = false)
    private String content;
    @Builder.Default
    @Column(name = "OPEN_YN", nullable = false)
    private Yn openYn = Yn.Y;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "projectNotice", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private java.util.List<ProjectNoticeView> projectNoticeViewList;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void toggleOpenYn() {
        this.openYn = this.openYn == Yn.Y ? Yn.N : Yn.Y;
    }
}
