package com.example.gitmanager.project.entity;

import com.example.gitmanager.file.entity.Files;
import com.example.gitmanager.util.entity.RecordTime;
import com.example.gitmanager.util.enums.ProjectType;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;

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
    @Column(name = "DESCRIPTION", length = 500)
    private String description;
    @Column(name = "URL", length = 300, nullable = false)
    private String url;
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private ProjectType type;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProjectMember> projectMemberList;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProjectLike> projectLikeList;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProjectView> projectViewList;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProjectMemberTemp> projectMemberTempList;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Files> filesList;

    public void toggleType() {
        this.type = this.type == ProjectType.PUBLIC ? ProjectType.PRIVATE : ProjectType.PUBLIC;
    }
}
