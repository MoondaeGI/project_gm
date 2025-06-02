package com.example.gitmanager.file.entity;

import com.example.gitmanager.file.dto.FilesDTO;
import com.example.gitmanager.notice.entity.Notice;
import com.example.gitmanager.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "files_seq_gene",
        sequenceName = "FILES_ID_SEQ",
        allocationSize = 1
)
@Table(name = "FILES")
@Entity
public class Files {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_seq_gene")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTICE_ID")
    private Notice notice;

    @OneToMany(mappedBy = "files", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FileDetail> fileDetailList;
}
