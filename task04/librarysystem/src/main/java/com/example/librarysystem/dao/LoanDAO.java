package com.example.librarysystem.dao;

import com.example.librarysystem.model.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanDAO {
    Loan save(Loan loan);
    Optional<Loan> findById(Long id);
    List<Loan> findAll();
}
