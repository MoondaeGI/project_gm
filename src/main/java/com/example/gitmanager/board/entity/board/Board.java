package com.example.gitmanager.board.entity.board;

import com.example.gitmanager.board.dto.board.BoardUpdateDTO;
import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.util.entity.RecordTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "board_seq_gene",
        sequenceName = "BOARD_ID_SEQ",
        allocationSize = 1
)
@Table(name = "BOARD")
@Entity
public class Board extends RecordTime {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq_gene")
    private Long id;
    @Column(name = "TITLE", length = 300, nullable = false)
    private String title;
    @Column(name = "CONTENT", columnDefinition = "CLOB", nullable = false)
    private String content;
    @Builder.Default
    @Column(name = "VIEW_COUNT", nullable = false)
    private int viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_CATEGORY_ID", nullable = false)
    private BoardCategory boardCategory;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_ID", nullable = false)
    private ProjectMember writer;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reply> replyList;

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(BoardUpdateDTO dto, BoardCategory boardCategory) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.boardCategory = boardCategory;

    }
}
