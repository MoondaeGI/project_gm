package com.example.gitmanager.member.service;

import com.example.gitmanager.member.dto.member.LoginDTO;
import com.example.gitmanager.member.dto.member.LoginResultDTO;
import com.example.gitmanager.member.dto.member.MemberUpdateDTO;
import com.example.gitmanager.member.dto.member.SignInDTO;

import java.io.IOException;

public interface MemberService {
    LoginResultDTO login(LoginDTO dto);
    void signIn(SignInDTO dto) throws IOException;
    void update(MemberUpdateDTO dto, String loginId) throws IOException;
    void signOut(long id, String loginId);
    boolean isLoginIdDuplicate(String loginId);
    boolean isEmailDuplicate(String email);
    boolean isNameDuplicate(String name);
}
