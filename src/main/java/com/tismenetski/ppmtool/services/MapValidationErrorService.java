package com.tismenetski.ppmtool.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

//A Class responsible for extracting the binding result errors (For example: @NotBlank, @Email, @Size) and return a ResponseEntity Object with the errors
@Service
public class MapValidationErrorService {


    public ResponseEntity<?> MapValidationService(BindingResult result)
    {
        if (result.hasErrors()) //Check result for errors
        {
            Map<String,String> errorMap = new HashMap<>(); //create a map to hold the results
            for (FieldError error : result.getFieldErrors()) // for each error
            {
                errorMap.put(error.getField(),error.getDefaultMessage()); //extract the error field and his message and place in the map
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST); //return the map with a bad request HttpStatus
        }
        return null; //return null errors
    }
}
