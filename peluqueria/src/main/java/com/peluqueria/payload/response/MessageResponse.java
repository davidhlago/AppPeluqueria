package com.peluqueria.payload.response;

public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    // --- Getters y Setters ---
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}