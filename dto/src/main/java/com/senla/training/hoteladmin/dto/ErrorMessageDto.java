package com.senla.training.hoteladmin.dto;

public class ErrorMessageDto {

    private String exceptionType;
    private String reason;

    public ErrorMessageDto(String exceptionType, String reason) {
        this.exceptionType = exceptionType;
        this.reason = reason;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
