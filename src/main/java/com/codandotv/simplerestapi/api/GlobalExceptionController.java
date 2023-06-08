package com.codandotv.simplerestapi.api;

import com.codandotv.simplerestapi.api.response.HealthResponse;
import com.codandotv.simplerestapi.api.response.ValidationFieldsResponse;
import com.codandotv.simplerestapi.domain.exception.NoContentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationFieldsResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var fieldErrors = e.getBindingResult().getFieldErrors();
        var globalErrors = e.getBindingResult().getGlobalErrors();

        List<String> validationErrors = new ArrayList<>();

        for(FieldError error : fieldErrors){
            validationErrors.add("Field: '" + error.getField() + "' Message: "+ error.getDefaultMessage());
        }
        for(ObjectError error : globalErrors) {
            validationErrors.add("Object: '" + error.getObjectName() + "' Message: "+ error.getDefaultMessage());
        }

        ValidationFieldsResponse response = new ValidationFieldsResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                validationErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity handleNoContentException(NoContentException e){
        return ResponseEntity.noContent().build();
    }
}
