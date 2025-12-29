package com.example.demo.service;

import com.example.demo.dto.ImportResult;
import com.example.demo.model.Income;
import com.example.demo.model.IncomeCategory;
import com.example.demo.model.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ImportService {

    private final UserService userService;
    private final IncomeService incomeService;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public ImportService(UserService userService, IncomeService incomeService) {
        this.userService = userService;
        this.incomeService = incomeService;
    }

    public ImportResult importIncomes(MultipartFile file) {
        List<String> messages = new ArrayList<>();
        int success = 0;
        int errors = 0;
        List<Income> incomesToSave = new ArrayList<>();
        User user = userService.getUserFromToken();


        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);


            List<IncomeCategory> categories  =
                    incomeService.findAllIncomeCategories();

            Map<String, IncomeCategory> categoryMap = categories.stream()
                    .collect(Collectors.toMap(
                            c -> c.getName(),
                            Function.identity()
                    ));
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                Income income = new Income();
                Cell cell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (cell == null) {
                    messages.add("Rekord " + i + " – brak kategorii");
                    errors++;
                    continue;
                }
                String name = row.getCell(0).getStringCellValue();
                IncomeCategory incomeCategory = categoryMap.get(name);
                if (incomeCategory == null) {
                    messages.add("Rekord " + i + " – nieistniejąca kategoria");
                    errors++;
                    continue;
                }

                income.setUser(user);
                income.setIncomeCategory(incomeCategory);

                LocalDate date = parseDate(row.getCell(1), i, messages);
                Double amount = parseAmount(row.getCell(2), i, messages);
                String comment = parseText(row.getCell(3), i, messages);
                if (date == null || amount == null) {
                    errors++;
                    continue;
                }
                income.setDateOfIncome(date);
                income.setAmount(amount);
                income.setIncomeComment(comment);
                incomesToSave.add(income);
                success++;
                messages.add("Rekord " + i + " zapisany poprawnie");

            }
            incomeService.saveAllIncomes(incomesToSave);
            return new ImportResult(success, errors, messages);

        } catch (IOException e) {

            return new ImportResult(success, errors, messages);
        }
    }


    private LocalDate parseDate(Cell cell, int rowNum, List<String> messages) {
        if (cell == null) {
            messages.add("Rekord " + rowNum + " – brak daty");
            return null;
        }

        try {
            // Excel date (numeric)
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }

            // Text date (e.g. 2024-01-31)
            String value = new DataFormatter().formatCellValue(cell).trim();
            if (value.isBlank()) {
                messages.add("Rekord " + rowNum + " – brak daty");
                return null;
            }

            return LocalDate.parse(value);

        } catch (Exception e) {
            messages.add("Rekord " + rowNum + " – niepoprawna data");
            return null;
        }
    }
    private Double parseAmount(Cell cell, int rowNum, List<String> messages) {
        try {
            String value = new DataFormatter().formatCellValue(cell);
            if (value.isBlank()) {
                messages.add("Rekord " + rowNum + " – brak kwoty");
                return null;
            }
            double amount = Double.parseDouble(value.replace(",", "."));
            if (amount <= 0) {
                messages.add("Rekord " + rowNum + " – kwota musi być > 0");
                return null;
            }
            return amount;
        } catch (Exception e) {
            messages.add("Rekord " + rowNum + " – niepoprawna kwota");
            return null;
        }
    }

    private String parseText(Cell cell, int rowNum, List<String> messages) {
        String value = new DataFormatter().formatCellValue(cell);
        if (value.length() > 255) {
            messages.add("Rekord " + rowNum + " – komentarz za długi");
            return null;
        }
        return value;
    }
}
