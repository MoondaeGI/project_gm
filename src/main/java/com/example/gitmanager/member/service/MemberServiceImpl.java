package com.example.gitmanager.member.service;

import com.example.gitmanager.member.dto.member.LoginDTO;
import com.example.gitmanager.member.dto.member.LoginResultDTO;
import com.example.gitmanager.member.dto.member.MemberUpdateDTO;
import com.example.gitmanager.member.dto.member.SignInDTO;
import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.entity.Token;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.member.repository.TokenRepository;
import com.example.gitmanager.util.enums.ROLE;
import com.example.gitmanager.util.enums.SignInType;
import com.example.gitmanager.util.exception.UnAuthenticationException;
import com.example.gitmanager.util.util.FileUtil;
import com.example.gitmanager.util.util.JWTUtil;
import com.example.gitmanager.util.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final JWTUtil jwtUtil;
    private final PasswordUtil passwordUtil;
    private final FileUtil fileUtil;

    @Transactional
    @Override
    public LoginResultDTO login(LoginDTO dto) {
        Member member = memberRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s를 가진 회원은 존재하지 않습니다.", dto.getLoginId())));

        if (passwordUtil.matches(dto.getPassword(), member.getPassword())) {
            String token = jwtUtil.createToken(dto.getLoginId(), List.of(member.getRole().toString()));

            tokenRepository.findByMember(member).ifPresent(tokenRepository::delete);
            String refreshToken = jwtUtil.createRefreshToken(dto.getLoginId());
            tokenRepository.save(
                    Token.builder()
                            .member(member)
                            .token(refreshToken)
                            .build());

            return LoginResultDTO.builder()
                    .id(member.getId())
                    .role(member.getRole().toString())
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();
        }

        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    @Transactional
    @Override
    public void signIn(SignInDTO dto) throws IOException {
        String password = passwordUtil.encodePassword(dto.getPassword());

        if (dto.getMultipartFile() != null) {
            String profileImg = fileUtil.uploadProfileImg(dto.getMultipartFile());
            dto.setProfileImg(profileImg);
        }

        memberRepository.save(Member.builder()
                .name(dto.getName())
                .loginId(dto.getLoginId())
                .password(password)
                .email(dto.getEmail())
                .profileImg(dto.getProfileImg())
                .role(ROLE.USER)
                .signInType(SignInType.NONE)
                .build());
    }

    @Transactional
    @Override
    public void update(MemberUpdateDTO dto, String loginId) throws IOException {
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s를 가진 회원은 존재하지 않습니다.", loginId)));

        Member member = memberRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d를 가진 회원은 존재하지 않습니다.", dto.getId())));

        if (Objects.equals(member.getId(), loginMember.getId()) || loginMember.getRole().equals(ROLE.ADMIN)) {
            if (dto.getMultipartFile() != null) {
                fileUtil.delete(member.getProfileImg());  // 기존 프로필 이미지 삭제
                String profileImg = fileUtil.uploadProfileImg(dto.getMultipartFile());  // 새로운 프로필 이미지 업로드

                dto.setProfileImg(profileImg);
            }

            member.update(dto);
        } else {
            throw new UnAuthenticationException();
        }
    }

    @Transactional
    @Override
    public void signOut(long id, String loginId) {
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s를 가진 회원은 존재하지 않습니다.", loginId)));

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d를 가진 회원은 존재하지 않습니다.", id)));

        if (Objects.equals(member.getId(), loginMember.getId()) || loginMember.getRole().equals(ROLE.ADMIN)) {
            fileUtil.delete(member.getProfileImg());
            memberRepository.delete(member);
        } else {
            throw new UnAuthenticationException();
        }
    }

    @Override
    public boolean isLoginIdDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public boolean isNameDuplicate(String name) {
        return memberRepository.existsByName(name);
    }
}
