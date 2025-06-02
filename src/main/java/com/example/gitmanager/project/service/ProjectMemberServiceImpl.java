package com.example.gitmanager.project.service;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.project.entity.ProjectMemberTemp;
import com.example.gitmanager.project.repository.ProjectMemberRepository;
import com.example.gitmanager.project.repository.ProjectMemberTempRepository;
import com.example.gitmanager.project.repository.ProjectRepository;
import com.example.gitmanager.util.enums.ROLE;
import com.example.gitmanager.util.enums.Yn;
import com.example.gitmanager.util.exception.UnAuthenticationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberTempRepository projectMemberTempRepository;

    @Override
    public void insert(long projectId, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트가 없습니다.", projectId)));
        ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(project, member);

        if (projectMember != null) {
            ProjectMemberTemp projectMemberTemp = projectMemberTempRepository.findByProjectAndMember(project, member);

            if (projectMemberTemp == null) {
                throw new IllegalArgumentException("해당 회원은 초대받지 못했습니다.");
            } else {
                projectMemberTempRepository.delete(projectMemberTemp);
                projectMemberRepository.save(ProjectMember.builder()
                        .project(project)
                        .member(member)
                        .leaderYn(Yn.N)
                        .build());
            }
        } else {
            throw new IllegalArgumentException("해당 회원은 이미 프로젝트 회원입니다.");
        }
    }

    @Transactional
    @Override
    public void delete(long projectId, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트가 없습니다.", projectId)));
        ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(project, member);

        if (projectMember != null) {
            if ((projectMember.getMember().getId() == member.getId() || projectMember.getLeaderYn().equals(Yn.Y))
                    || member.getRole().equals(ROLE.ADMIN)) {
                projectMemberRepository.delete(projectMember);
                return;
            }
        }
        throw new UnAuthenticationException();
    }

    @Transactional
    @Override
    public void toggleLeader(long projectId, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트가 없습니다.", projectId)));
        ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(project, member);

        if (projectMember != null) {
            if (projectMember.getMember().getId() == member.getId() || projectMember.getLeaderYn().equals(Yn.Y)) {
                projectMember.toggleLeaderYn();
                return;
            }
        }
        throw new UnAuthenticationException();
    }
}
