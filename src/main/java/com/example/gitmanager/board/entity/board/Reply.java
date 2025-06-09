package com.example.gitmanager.board.entity.board;

import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.util.entity.RecordTime;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "reply_seq_gnen",
        sequenceName = "REPLY_ID_SEQ",
        allocationSize = 1
)
@Table(name = "REPLY")
@Entity
public class Reply extends RecordTime {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reply_seq_gnen")
    private Long id;
    private String content;
    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPLY_ID")
    private Reply parent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", nullable = false)
    private Board board;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_ID", nullable = false)
    private ProjectMember writer;

    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private java.util.List<Reply> children;

    public void update(String content) {
        this.content = content;
    }
}
