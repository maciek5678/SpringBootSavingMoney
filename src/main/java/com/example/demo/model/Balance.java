package com.example.demo.model;

import java.util.List;

public class Balance {
    private List<Expense> expenseList;
    private List<Income> incomesList;


    public Balance(List<Expense> expenseList, List<Income> incomesList) {
        this.expenseList = expenseList;
        this.incomesList = incomesList;
    }


    public  Double getBalance() {
         Double sum = 0.00;
        for (Expense expense : expenseList) {
            sum -=expense.getAmount();

        }

        for (Income income : incomesList) {
            sum -=income.getAmount();

        }
        return sum;

    }







}
