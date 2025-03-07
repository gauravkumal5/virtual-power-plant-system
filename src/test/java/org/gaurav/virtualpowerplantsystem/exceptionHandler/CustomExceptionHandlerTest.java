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
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        var ex = mock(MethodArgumentNotValidException.class);
        var bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        var error = new ObjectError("field", "Invalid field");
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(error));

        var responseEntity = customExceptionHandler.handleMethodArgumentNotValid(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, mock(WebRequest.class));

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ApiResponse<List<String>> apiResponse = (ApiResponse<List<String>>) responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("Validation Failed", apiResponse.getMessage());
    }

    @Test
    void whenExceptionOccurred_handleAllExceptions_thenReturnResponse() {
        var ex = mock(Exception.class);
        var response = mock(ResponseEntity.class);

        try (MockedStatic<ResponseUtility> utilities = Mockito.mockStatic(ResponseUtility.class)) {
            utilities.when(ResponseUtility::internalServerErrorResponse).thenReturn(response);

            var responseEntity = customExceptionHandler.handleAllExceptions(ex);
            assertEquals(response, responseEntity);
        }
    }

}
