package com.amar.account.exceptionHandler;

import com.amar.account.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
//@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    private ResponseEntity<ExceptionResponseDto> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException exception, WebRequest webRequest) {

        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                webRequest.getDescription(false), // WebRequest can be used to get API details.
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    private ResponseEntity<ExceptionResponseDto> handleCustomerNotFoundException(CustomerNotFoundException exception, WebRequest webRequest){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    private  ResponseEntity<ExceptionResponseDto> globalExceptionHandler(Exception exception,WebRequest webRequest){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(exceptionResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
