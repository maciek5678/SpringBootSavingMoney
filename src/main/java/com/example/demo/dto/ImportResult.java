package com.example.demo.dto;

import java.util.List;

public record ImportResult(
        int success,
        int errors,
        List<String> messages
) {}
