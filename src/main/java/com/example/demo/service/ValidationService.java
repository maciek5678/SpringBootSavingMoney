package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public  class ValidationService {
        public static void validateDates(LocalDate startDate, LocalDate endDate) {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
        }
}
