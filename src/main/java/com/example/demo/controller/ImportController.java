package com.example.demo.controller;


import com.example.demo.service.ImportService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
public class ImportController {

private final ImportService importService;

        public ImportController(ImportService importService) {
            this.importService = importService;
        }


        @PostMapping(value = "/incomes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<?> importIncomes(@RequestParam(value = "file", required = false) MultipartFile file) {
            if (file == null || file.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Plik nie został przesłany");
            }

            return ResponseEntity.ok(importService.importIncomes(file));
        }

    }
