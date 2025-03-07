package org.gaurav.virtualpowerplantsystem.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.gaurav.virtualpowerplantsystem.model.response.ApiResponse;
import org.gaurav.virtualpowerplantsystem.utility.ResponseUtility;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
       var details = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList();

       var validationFailed = new ApiResponse<>("Validation Failed", details);
        return new ResponseEntity<>(validationFailed, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<ApiResponse<T>>  handleAllExceptions(Exception e) {
        log.error("Exception occurred: ", e);
        return ResponseUtility.internalServerErrorResponse();
    }

}