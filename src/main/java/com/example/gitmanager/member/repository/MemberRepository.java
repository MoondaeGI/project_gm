package com.example.gitmanager.member.repository;

import com.example.gitmanager.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);
    boolean existsByName(String name);
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
}
