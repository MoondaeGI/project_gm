package com.example.gitmanager.member.repository;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByMember(Member member);
    Optional<Token> findByToken(String token);
}
