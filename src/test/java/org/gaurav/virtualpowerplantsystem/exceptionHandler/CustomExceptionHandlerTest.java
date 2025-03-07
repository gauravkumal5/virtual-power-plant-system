package org.gaurav.virtualpowerplantsystem.exceptionHandler;

import org.gaurav.virtualpowerplantsystem.model.response.ApiResponse;
import org.gaurav.virtualpowerplantsystem.utility.ResponseUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomExceptionHandlerTest {

    private CustomExceptionHandler customExceptionHandler;

    @BeforeEach
    public void setUp() {
        customExceptionHandler = new CustomExceptionHandler();
    }

    @Test
    void whenArgumentInvalid_handleMethodArgumentNotValid_thenReturnsInvalidResponse() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ObjectError error = new ObjectError("field", "Invalid field");
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(error));

        ResponseEntity<Object> responseEntity = customExceptionHandler.handleMethodArgumentNotValid(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, mock(WebRequest.class));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ApiResponse<List<String>> apiResponse = (ApiResponse<List<String>>) responseEntity.getBody();
        assertEquals("Validation Failed", apiResponse.message());
    }

    @Test
    void whenExceptionOccurred_handleAllExceptions_thenReturnResponse() {
        Exception ex = mock(Exception.class);

        ResponseEntity<ApiResponse<Object>> response = mock(ResponseEntity.class);

        try (MockedStatic<ResponseUtility> utilities = Mockito.mockStatic(ResponseUtility.class)) {
            utilities.when(ResponseUtility::internalServerErrorResponse).thenReturn(response);

            ResponseEntity<ApiResponse<Object>> responseEntity = customExceptionHandler.handleAllExceptions(ex);
            assertEquals(response, responseEntity);
        }
    }

}
