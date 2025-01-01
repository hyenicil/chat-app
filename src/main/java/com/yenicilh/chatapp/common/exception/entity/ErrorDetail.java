package com.yenicilh.chatapp.common.exception.entity;

import java.time.LocalDateTime;

public class ErrorDetail {

    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorDetail() {}

    public ErrorDetail(String error, String message, LocalDateTime timestamp) {
        super();
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }
}
