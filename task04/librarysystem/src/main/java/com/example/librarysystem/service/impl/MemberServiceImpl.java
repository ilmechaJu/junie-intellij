package com.example.librarysystem.service.impl;

import com.example.librarysystem.dao.MemberDAO;
import com.example.librarysystem.model.Member;
import com.example.librarysystem.service.MemberService;

import java.util.List;
import java.util.Optional;

public class MemberServiceImpl implements MemberService {
    private final MemberDAO memberDAO;

    public MemberServiceImpl(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    @Override
    public Member addOrUpdate(Member member) {
        return memberDAO.save(member);
    }

    @Override
    public Optional<Member> get(Long id) {
        return memberDAO.findById(id);
    }

    @Override
    public List<Member> list() {
        return memberDAO.findAll();
    }

    @Override
    public boolean remove(Long id) {
        return memberDAO.deleteById(id);
    }
}
