package com.github.naratipudx.auth.dto;

public class ErrorResp {

    private String message;

    public ErrorResp(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
