package com.example.gitmanager.project.repository;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAll(@NonNull Pageable pageable);
    int countBy();

    @Query(value = "SELECT p " +
                    "FROM Project p " +
                    "JOIN p.projectMemberList pm ON p = pm.project " +
                    "WHERE pm.member = :member",
            countQuery = "SELECT COUNT(p) " +
                    "FROM Project p " +
                    "JOIN p.projectMemberList pm ON p = pm.project " +
                    "WHERE pm.member = :member"
    )
    Page<Project> findByMember(Member member, @NonNull Pageable pageable);

    @Query("SELECT COUNT(*) " +
            "FROM Project p " +
            "JOIN ProjectMember pm ON p = pm.project " +
            "WHERE pm.member = :member")
    int countByMember(Member member);
}
