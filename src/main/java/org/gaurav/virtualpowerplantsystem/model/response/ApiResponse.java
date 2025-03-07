package org.gaurav.virtualpowerplantsystem.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;

    // Constructor, getters, setters
    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

}

