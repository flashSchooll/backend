package com.flashcard.service.excel;

import lombok.Getter;

@Getter
public class InvalidCellException extends RuntimeException {

    private final String columnName;
    private final String value;

    public InvalidCellException(String columnName, String value, Throwable cause) {
        super(columnName + " kolonunda " + value + " değeri hatalı", cause);
        this.columnName = columnName;
        this.value = value;
    }
}
