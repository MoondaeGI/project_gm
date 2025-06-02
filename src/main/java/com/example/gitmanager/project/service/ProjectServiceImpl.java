package com.example.gitmanager.project.service;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.project.dto.ProjectDTO;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.project.repository.ProjectMemberRepository;
import com.example.gitmanager.project.repository.ProjectRepository;
import com.example.gitmanager.util.enums.ProjectType;
import com.example.gitmanager.util.enums.ROLE;
import com.example.gitmanager.util.enums.Yn;
import com.example.gitmanager.util.exception.UnAuthenticationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<ProjectDTO> findAll(int page) {
        int start = (page - 1) * 10;
        int end = Math.min(page * 10, projectRepository.countBy());
        PageRequest pageRequest = PageRequest.of(start, end, Sort.by(Sort.Direction.DESC, "id"));

        return projectRepository.findAll(pageRequest).stream()
                .map(ProjectDTO::of)
                .toList();
    }

    @Transactional
    @Override
    public List<ProjectDTO> findByMemberId(long memberId, int page) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 맴버가 존재하지 않습니다.", memberId)));

        int start = (page - 1) * 10;
        int end = Math.min(start + 10, projectRepository.countByMember(member));
        PageRequest pageRequest = PageRequest.of(start, end, Sort.by(Sort.Direction.DESC, "id"));

        return projectRepository.findByMember(member, pageRequest)
                .getContent().stream()
                .map(ProjectDTO::of)
                .toList();
    }

    @Transactional
    @Override
    public ProjectDTO findById(long id, String loginId) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트가 없습니다.", id)));
        ProjectType projectType = project.getType();

        if (loginId != null) {
            Member member = memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));
            ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(project, member);

            if ((projectMember != null || member.getRole().equals(ROLE.ADMIN)) || projectType.equals(ProjectType.PUBLIC)) {
                return ProjectDTO.of(project);
            }
            throw new UnAuthenticationException();
        }

        if (projectType.equals(ProjectType.PUBLIC)) {
            return ProjectDTO.of(project);
        }
        throw new UnAuthenticationException();
    }

    @Override
    public void insert(String url) {

    }

    @Override
    public void update(long id, String loginId) {

    }

    @Transactional
    @Override
    public void delete(long id, String loginId) {
        if (loginId != null) {
            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("%d의 번호를 가진 프로젝트가 없습니다.", id)));

            Member member = memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));
            ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(project, member);

            if (projectMember.getLeaderYn().equals(Yn.Y) || member.getRole().equals(ROLE.ADMIN)) {
                projectRepository.delete(project);
            }
            throw new UnAuthenticationException();
        }

        throw new UnAuthenticationException();
    }
}
