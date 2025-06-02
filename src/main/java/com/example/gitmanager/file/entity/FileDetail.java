package com.example.gitmanager.file.entity;

import com.example.gitmanager.file.dto.FileDetailDTO;
import com.example.gitmanager.util.entity.RecordTime;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "file_detail_seq_gene",
        sequenceName = "FILE_DETAIL_ID_SEQ",
        allocationSize = 1
)
@Table(name = "FILE_DETAIL")
@Entity
public class FileDetail extends RecordTime {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_detail_seq_gene")
    private Long id;
    @Column(name = "ORIGIN_FILE_NAME", length = 200, nullable = false)
    private String originFileName;
    @Column(name = "SYSTEM_FILE_NAME", length = 300, nullable = false)
    private String systemFileName;
    @Column(name = "PATH", length = 300, nullable = false)
    private String path;
    @Column(name = "FILE_SIZE", nullable = false)
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILES_ID", nullable = false)
    private Files files;

    public static FileDetail of(FileDetailDTO dto, Files files) {
        return FileDetail.builder()
                .originFileName(dto.getOriginFileName())
                .systemFileName(dto.getSystemFileName())
                .path(dto.getPath())
                .fileSize(dto.getFileSize())
                .files(files)
                .build();
    }
}
