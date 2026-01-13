package com.cinemaabyss.proxy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private boolean success;
    private Object data;
    private String message;
    private String error;
    private String status;

    public ApiResponse(boolean success, Object data, String message, String error, String status) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.error = error;
        this.status = status;
    }

    public ApiResponse() {
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}