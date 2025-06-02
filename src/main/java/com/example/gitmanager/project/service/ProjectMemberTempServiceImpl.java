package com.example.gitmanager.project.service;

import com.example.gitmanager.member.dto.member.MemberDTO;
import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.project.dto.ProjectDTO;
import com.example.gitmanager.project.dto.ProjectMemberTempDTO;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.project.entity.ProjectMemberTemp;
import com.example.gitmanager.project.repository.ProjectMemberRepository;
import com.example.gitmanager.project.repository.ProjectMemberTempRepository;
import com.example.gitmanager.project.repository.ProjectRepository;
import com.example.gitmanager.util.exception.UnAuthenticationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectMemberTempServiceImpl implements ProjectMemberTempService {
    private final ProjectMemberTempRepository projectMemberTempRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<MemberDTO> findByProjectId(long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트가 없습니다.", projectId)));

        return projectMemberRepository.findByProject(project).stream()
                .map(projectMember -> MemberDTO.of(projectMember.getMember()))
                .toList() ;
    }

    @Override
    public List<ProjectDTO> findByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

        return projectMemberTempRepository.findByMember(member).stream()
                .map(projectMemberTemp -> ProjectDTO.of(projectMemberTemp.getProject()))
                .toList();
    }

    @Transactional
    @Override
    public void insert(ProjectMemberTempDTO dto, String loginId) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트가 없습니다.", dto.getProjectId())));

        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));
        ProjectMember loginProjectMember = projectMemberRepository.findByProjectAndMember(project, loginMember);

        if (loginProjectMember != null) {
            Member member = memberRepository.findById(dto.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("%d의 번호를 가진 회원이 없습니다.", dto.getMemberId())));
            ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(project, member);

            if (projectMember != null) {
                throw new IllegalArgumentException("이미 참여중인 프로젝트입니다.");
            }

            ProjectMemberTemp projectMemberTemp = projectMemberTempRepository.findByProjectAndMember(project, member);

            if (projectMemberTemp != null) {
                projectMemberTempRepository.save(ProjectMemberTemp.builder()
                        .project(project)
                        .member(member)
                        .build());
            } else {
                throw new IllegalArgumentException("이미 초대중인 회원입니다.");
            }
        } else {
            throw new UnAuthenticationException();
        }
    }

    @Transactional
    @Override
    public void delete(long id, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

        ProjectMemberTemp projectMemberTemp = projectMemberTempRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 대기 회원이 없습니다.", id)));

        if (member.getId() == projectMemberTemp.getMember().getId()) {
            projectMemberTempRepository.delete(projectMemberTemp);
        } else {
            throw new UnAuthenticationException();
        }
    }
}
