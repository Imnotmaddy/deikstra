package com.itechart.alifanov.deikstra.service.exception;

public class DeikstraAppException extends Exception {
    private String status;

    public DeikstraAppException(String message, String status) {
        super(message);
        this.status = status;
    }
}
