package com.example.gitmanager.project.entity;

import com.example.gitmanager.util.entity.RecordTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "project_seq_gene",
        sequenceName = "PROJECT_ID_SEQ",
        allocationSize = 1
)
@Table(name = "PROJECT")
@Entity
public class Project extends RecordTime {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq_gene")
    private Long id;
    @Column(name = "NAME", length = 100, nullable = false)
    private String name;
    @Column(name = "DESCRIPTION", length = 500, nullable = false)
    private String description;
    @Column(name = "URL", length = 300, nullable = false)
    private String url;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProjectMember> projectMemberList;
}
