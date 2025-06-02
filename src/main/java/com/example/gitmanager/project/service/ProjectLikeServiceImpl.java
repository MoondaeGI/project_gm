package com.example.gitmanager.project.service;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.entity.ProjectLike;
import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.project.repository.ProjectLikeRepository;
import com.example.gitmanager.project.repository.ProjectMemberRepository;
import com.example.gitmanager.project.repository.ProjectRepository;
import com.example.gitmanager.util.exception.UnAuthenticationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectLikeServiceImpl implements ProjectLikeService {
    private final ProjectLikeRepository projectLikeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void insert(long projectId, String loginId) {
        if (loginId != null) {
            Member member = memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("%d의 번호를 가진 프로젝트가 없습니다.", loginId)));
            ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(project, member);

            if (projectMember != null) {
                throw new UnAuthenticationException("자신의 프로젝트는 좋아요를 할 수 없습니다.");
            }

            ProjectLike projectLike = projectLikeRepository.findByProjectAndMember(project, member);
            if (projectLike == null) {
                projectLike = ProjectLike.builder()
                        .project(project)
                        .member(member)
                        .build();
                projectLikeRepository.save(projectLike);
            }

            throw new UnAuthenticationException("이미 좋아요를 한 프로젝트 입니다.");
        }

        throw new UnAuthenticationException();
    }

    @Transactional
    @Override
    public void delete(long projectId, String loginId) {
        if (loginId != null) {
            Member member = memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("%d의 번호를 가진 프로젝트가 없습니다.", loginId)));
            ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(project, member);

            if (projectMember != null) {
                throw new UnAuthenticationException("자신의 프로젝트는 좋아요를 할 수 없습니다.");
            }

            ProjectLike projectLike = projectLikeRepository.findByProjectAndMember(project, member);
            if (projectLike != null) {
                projectLikeRepository.delete(projectLike);
            }

            throw new UnAuthenticationException("이미 좋아요를 하지 않은 프로젝트입니다.");
        }

        throw new UnAuthenticationException();
    }
}
