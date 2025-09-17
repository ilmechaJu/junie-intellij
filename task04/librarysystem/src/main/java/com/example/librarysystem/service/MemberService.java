package com.example.librarysystem.service;

import com.example.librarysystem.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member addOrUpdate(Member member);
    Optional<Member> get(Long id);
    List<Member> list();
    boolean remove(Long id);
}
