package com.example.demo.repository;

import com.example.demo.model.IncomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {

    Optional<IncomeCategory> findById(int Id);
    Optional<IncomeCategory> findByName(String name);
    List<IncomeCategory> findAll();
}
