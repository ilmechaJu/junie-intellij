package com.example.librarysystem.controller;

import com.example.librarysystem.model.Member;
import com.example.librarysystem.service.MemberService;

import java.util.List;

public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    public Member addMember(String name, String email, String phone) {
        Member member = Member.builder().name(name).email(email).phone(phone).build();
        return memberService.addOrUpdate(member);
    }

    public List<Member> listMembers() {
        return memberService.list();
    }
}
