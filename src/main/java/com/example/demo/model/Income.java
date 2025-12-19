package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "incomes")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "income_category_id", nullable = false)
    @JsonIgnoreProperties("incomes")
    private IncomeCategory incomeCategory;



    @Column(nullable = false)
    private Double amount;

    @Column(name = "date_of_income", nullable = false)
    private LocalDate dateOfIncome;

    @Column(name = "income_comment", nullable = false, length = 100)
    private String incomeComment;



    // getters & setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public IncomeCategory getIncomeCategory() { return incomeCategory; }
    public void setIncomeCategory(IncomeCategory incomeCategory) { this.incomeCategory = incomeCategory; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public LocalDate getDateOfIncome() { return dateOfIncome; }
    public void setDateOfIncome(LocalDate dateOfExpense) { this.dateOfIncome = dateOfExpense; }
    public String getIncomeComment() { return incomeComment; }
    public void setIncomeComment(String expenseComment) { this.incomeComment = expenseComment; }
}

