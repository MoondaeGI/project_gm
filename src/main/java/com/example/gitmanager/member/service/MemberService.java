package com.example.gitmanager.member.service;

import com.example.gitmanager.member.dto.member.LoginDTO;
import com.example.gitmanager.member.dto.member.LoginResultDTO;
import com.example.gitmanager.member.dto.member.MemberUpdateDTO;
import com.example.gitmanager.member.dto.member.SignInDTO;

public interface MemberService {
    LoginResultDTO login(LoginDTO dto);
    void signIn(SignInDTO dto);
    void update(MemberUpdateDTO dto);
    void signOut(long id);
}
