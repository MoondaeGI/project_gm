package com.example.gitmanager.member.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.entity.Token;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.member.repository.TokenRepository;
import com.example.gitmanager.util.exception.ExpiredRefreshTokenException;
import com.example.gitmanager.util.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    @Override
    public String reissue(String token) {
        Token refreshToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 유저가 발급받은 refresh token이 없습니다."));

        if (jwtUtil.validation(refreshToken.getToken()) && refreshToken.getReissueCount() < 10) {
            String loginId = jwtUtil.getLoginId(token);

            Member member = memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("%s를 가진 회원은 존재하지 않습니다.", loginId)));

            String reissueToken = jwtUtil.createToken(member.getLoginId(), List.of(member.getRole().toString()));
            refreshToken.increaseReissueCount();

            return reissueToken;
        }
        tokenRepository.delete(refreshToken);

        throw new ExpiredRefreshTokenException();
    }
}
