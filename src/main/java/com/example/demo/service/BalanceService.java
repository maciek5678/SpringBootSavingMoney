package com.example.demo.service;

import com.example.demo.model.Balance;
import com.example.demo.model.Expense;
import com.example.demo.model.Income;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class BalanceService {
IncomeService incomeService;
ExpenseService expenseService;
UserService userService;

    public BalanceService(IncomeService incomeService, ExpenseService expenseService, UserService userService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.userService = userService;
    }

    public Double getBalance (LocalDate startDate  , LocalDate endDate){
        User user = userService.getUserFromToken();
        List<Income> incomes;
        List<Expense> expenses;
        if(startDate == null && endDate == null) {
            incomes = incomeService.findByUserIdAndDateOfIncomeBetween(user.getId(), null, null);
            expenses = expenseService.findByUserIdAndDateOfExpenseBetween(user.getId(), null, null);
        }else {
            ValidationService.validateDates(startDate, endDate);
            incomes = incomeService.findByUserIdAndDateOfIncomeBetween(user.getId(), startDate, endDate);
            expenses = expenseService.findByUserIdAndDateOfExpenseBetween(user.getId(), startDate, endDate);
        }

        Double sum = 0.00;
        for (Expense expense : expenses) {
            sum -=expense.getAmount();

        }

        for (Income income : incomes) {
            sum -=income.getAmount();

        }
        return sum;

    }
}
