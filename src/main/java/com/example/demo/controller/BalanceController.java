package com.example.demo.controller;

import com.example.demo.model.Balance;
import com.example.demo.model.Expense;
import com.example.demo.model.Income;
import com.example.demo.model.User;
import com.example.demo.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/balance")
public class BalanceController {


    private final BalanceService balanceService;
    private final UserService userService;

    public BalanceController( BalanceService balanceService, UserService userService) {

        this.balanceService = balanceService;
        this.userService = userService;
    }

    @GetMapping
    public Double getBalance (){

        return balanceService.getBalance(null,null);

    }

    @GetMapping("/dates/{startDate}/{endDate}")
    public Double getBalanceBetweenDates (@PathVariable LocalDate startDate, @PathVariable LocalDate endDate){
        return balanceService.getBalance(startDate,endDate);


    }

}
