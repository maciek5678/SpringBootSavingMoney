package com.example.demo.service;

import com.example.demo.model.Income;
import com.example.demo.model.IncomeCategory;
import com.example.demo.model.User;
import com.example.demo.repository.IncomeCategoryRepository;
import com.example.demo.repository.IncomeRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    IncomeRepository incomeRepository;
    IncomeCategoryRepository incomeCategoryRepository;


    public IncomeService(IncomeRepository incomeRepository, IncomeCategoryRepository incomeCategoryRepository) {
        this.incomeRepository = incomeRepository;
        this.incomeCategoryRepository = incomeCategoryRepository;
    }

    public Income addIncome(Income income, User user , IncomeCategory incomeCategory){
        income.setUser(user);
        income.setIncomeCategory(incomeCategory);
        return incomeRepository.save(income);
    }

    public int deleteIncome(Long incomeiId, User user){
        return incomeRepository.deleteByUserIdAndId(user.getId(), incomeiId);

    }


    public IncomeCategory findById(int incomeCategoryId) {
        IncomeCategory incomeCategory = incomeCategoryRepository.findById(incomeCategoryId)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        return incomeCategory;
    }


    public List<Income> findByUserIdAndDateOfIncomeBetween(Long Id, LocalDate startDate, LocalDate endDate){
        List<Income> incomes;
        if(startDate == null && endDate == null) {
            incomes = incomeRepository.findByUserId(Id);
        }
        else {
            ValidationService.validateDates(startDate,endDate);
            incomes = incomeRepository.findByUserIdAndDateOfIncomeBetween(Id, startDate, endDate);
        }
        return incomes;
    }
    public Optional<IncomeCategory> incomeCategoryFindByName(String name) {
        return incomeCategoryRepository.findByName(name);
    }
    public Income save(Income income) {
         return incomeRepository.save(income);
    }
    public List<IncomeCategory> findAllIncomeCategories() {
        return incomeCategoryRepository.findAll();
    }

    public List<Income> saveAllIncomes(List<Income> incomes) {
       return  incomeRepository.saveAll(incomes);
    }

}
