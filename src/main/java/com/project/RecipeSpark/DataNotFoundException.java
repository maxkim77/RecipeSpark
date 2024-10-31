package com.project.RecipeSpark;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException {  // RuntimeException으로 변경
    private static final long serialVersionUID = 1L;

    public DataNotFoundException(String message) {
        super(message);
    }
}