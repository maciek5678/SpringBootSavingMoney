package com.example.demo.controller;


import com.example.demo.model.Income;
import com.example.demo.model.IncomeCategory;
import com.example.demo.model.User;
import com.example.demo.service.IncomeService;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/income")
public class IncomeController {


    private final IncomeService incomeService;

    private final UserService userService;

    public IncomeController(IncomeService incomeService, UserService userService) {
        this.incomeService = incomeService;
        this.userService = userService;
    }

    @GetMapping
    public List<Income> getIncomes() {
        User user = userService.getUserFromToken();
        return incomeService.findByUserIdAndDateOfIncomeBetween(user.getId(), null, null);

    }

    @GetMapping("/dates/{startDate}/{endDate}")
    public List<Income>  getExpenses(@PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        User user = userService.getUserFromToken();

        return incomeService.findByUserIdAndDateOfIncomeBetween(user.getId(), startDate, endDate);

    }


    @PostMapping("/{incomeCategoryId}")
    public Income addIncome( @RequestBody Income income, @PathVariable int incomeCategoryId) {

        User user = userService.getUserFromToken();
        IncomeCategory incomeCategory = incomeService.findById(incomeCategoryId);
        return incomeService.addIncome(income,user,incomeCategory);


    }
    @DeleteMapping("/delete/{incomeId}")
    public ResponseEntity<?> deleteIncome(@PathVariable long incomeId) {
        User user = userService.getUserFromToken();
        int income = incomeService.deleteIncome(incomeId, user);
        System.out.println("incomeId" + incomeId + "userId" + user.getId());
        return ResponseEntity.ok("Liczba usuniętych rekordów " +  income);
    }
}