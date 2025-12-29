package com.example.demo.security;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoHandlerFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Nie znaleziono ścieżki: " + ex.getRequestURL());
        body.put("status", 404);
        body.put("reqType", ex.getHttpMethod());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(FileUploadException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Plik nie został dołączony");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
