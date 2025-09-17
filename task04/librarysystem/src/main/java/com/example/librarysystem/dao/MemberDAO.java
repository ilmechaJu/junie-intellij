package com.example.librarysystem.dao;

import com.example.librarysystem.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberDAO {
    Member save(Member member);
    Optional<Member> findById(Long id);
    List<Member> findAll();
    boolean deleteById(Long id);
}
