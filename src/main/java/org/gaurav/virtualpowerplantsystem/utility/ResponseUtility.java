package org.gaurav.virtualpowerplantsystem.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.gaurav.virtualpowerplantsystem.model.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtility {

    public static <T> ResponseEntity<ApiResponse<T>> internalServerErrorResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("Sorry, some problem occurred.", null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> createdResponse(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(message, data));

    }

    public static <T> ResponseEntity<ApiResponse<T>> okWithDataResponse(String message, T data) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> okResponse(String message) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(message, null));
    }

}
