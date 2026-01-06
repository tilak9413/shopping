package org.example.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private int statusCode;
    private String message;
    private String jwtToken;
    private Object data;

    // Convenience constructor for success with token
    public ApiResponse(int statusCode, String message, String jwtToken) {
        this.statusCode = statusCode;
        this.message = message;
        this.jwtToken = jwtToken;
    }

    // Convenience constructor for error or simple message
    public ApiResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
