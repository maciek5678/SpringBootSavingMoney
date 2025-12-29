package com.example.demo.controller;


import com.example.demo.model.*;
import com.example.demo.service.ExpenseService;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {


    private final ExpenseService expenseService;

    private final UserService userService;

    public ExpenseController(ExpenseService expenseService, UserService userService) {

        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping
    public List<Expense>  getExpenses() {
        User user = userService.getUserFromToken();
        return expenseService.findByUserIdAndDateOfExpenseBetween(user.getId(), null, null);


    }

    @GetMapping("/dates/{startDate}/{endDate}")
    public List<Expense>  getExpenses(@PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        User user = userService.getUserFromToken();
        return expenseService.findByUserIdAndDateOfExpenseBetween(user.getId(), startDate, endDate);

    }

    @PostMapping("/{expenseCategoryId}/{paymentMethodId}")
    public Expense addExpense(@RequestBody Expense expense, @PathVariable int expenseCategoryId, @PathVariable int paymentMethodId) {

        User user = userService.getUserFromToken();
        ExpenseCategory expenseCategory = expenseService.findById(expenseCategoryId);
        PaymentMethod paymentMethod = expenseService.paymentFindById(paymentMethodId);
        return expenseService.addExpense(expense,user,expenseCategory, paymentMethod);


    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteIncome(@PathVariable long expenseId) {
        User user = userService.getUserFromToken();
        int income = expenseService.deleteExpense(expenseId, user);
        return ResponseEntity.ok("Liczba usuniętych rekordów " +  income);
    }
}
