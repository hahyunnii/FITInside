package com.team2.fitinside.global;

import com.team2.fitinside.order.exception.CartEmptyException;
import com.team2.fitinside.order.exception.OrderModificationNotAllowedException;
import com.team2.fitinside.order.exception.OrderNotFoundException;
import com.team2.fitinside.order.exception.OutOfStockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({OrderNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<String> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({OutOfStockException.class, OrderModificationNotAllowedException.class,
            NoSuchElementException.class})
    public ResponseEntity<String> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
    }

//    @ExceptionHandler(CategoryNotFoundException.class)
//    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(InvalidCategoryException.class)
//    public ResponseEntity<String> handleInvalidCategoryException(InvalidCategoryException ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationCredentialsNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다: " + e.getMessage());
    }


}
