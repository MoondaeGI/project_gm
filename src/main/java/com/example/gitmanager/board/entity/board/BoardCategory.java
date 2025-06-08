package com.example.gitmanager.board.entity.board;

import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.util.entity.RecordTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "board_ct_seq_gene",
        sequenceName = "BOARD_CATEGORY_ID_SEQ",
        allocationSize = 1
)
@Table(name = "BOARD_CATEGORY")
@Entity
public class BoardCategory extends RecordTime {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_ct_seq_gene")
    private Long id;
    @Builder.Default
    @Column(name = "DEPTH", nullable = false)
    private int depth = 0;
    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private BoardCategory parent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "boardCategory", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Board> boardList;

    public void update(String name) {
        this.name = name;
    }
}
