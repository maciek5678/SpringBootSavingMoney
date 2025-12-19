package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.ExpenseCategoryRepository;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.IncomeRepository;
import com.example.demo.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    ExpenseRepository expenseRepository;
    ExpenseCategoryRepository expenseCategoryRepository;
    PaymentMethodRepository paymentMethodRepository;
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense addExpense(Expense expense, User user , ExpenseCategory expenseCategory, PaymentMethod paymentMethod){
        expense.setUser(user);
        expense.setExpenseCategory(expenseCategory);
        expense.setPaymentMethodId(paymentMethod);
        return expenseRepository.save(expense);
    }

    public ExpenseCategory findById(int expenseCategoryId) {
        ExpenseCategory expenseCategory = expenseCategoryRepository.findById(expenseCategoryId)
                .orElseThrow(() -> new RuntimeException("Expense category not found"));

        return expenseCategory;
    }

    public PaymentMethod paymentFindById(int paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return paymentMethod;
    }


    public List<Expense> findByUserIdAndDateOfExpenseBetween(Long Id, LocalDate startDate, LocalDate endDate){

        List<Expense> expenses;
        if(startDate == null && endDate == null) {
            expenses = expenseRepository.findByUserId(Id);
        } else {
            ValidationService.validateDates(startDate,endDate);
            expenses = expenseRepository.findByUserIdAndDateOfExpenseBetween(Id, startDate, endDate);
        }
        return expenses;
    }


    public int deleteExpense(Long expenseId, User user){
        return expenseRepository.deleteByUserIdAndId(user.getId(), expenseId);

    }

}
