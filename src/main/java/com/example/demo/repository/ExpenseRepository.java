package com.example.demo.repository;

import com.example.demo.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long user_id);
    List<Expense> findByUserIdAndDateOfExpenseBetween(Long user_id, LocalDate startDate, LocalDate endDate);

    @Modifying
    @Transactional
    @Query("""
        delete from Expense i
        where i.user.id = :userId
          and i.id = :id
    """)
    int deleteByUserIdAndId(@Param("userId") Long userId,
                            @Param("id") Long id);
}
