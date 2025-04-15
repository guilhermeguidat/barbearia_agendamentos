package com.barbearia.agendamentos.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Tratando exceções gerais
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "erro", ex.getMessage(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "detalhes", "Algo deu errado, por favor, tente novamente."
                ));
    }

    // Tratando exceções específicas (exemplo: quando não encontrar um cliente)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", ex.getMessage(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "detalhes", "O recurso solicitado não foi encontrado."
                ));
    }

    // Exemplo de tratamento para violação de constraints (validação de dados)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "erro", "Dados inválidos.",
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "detalhes", ex.getMessage()
                ));
    }
}