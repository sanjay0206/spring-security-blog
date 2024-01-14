package com.springboot.blog.responses;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ErrorDetailsResponse {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime timestamp;
    private final String message;
    private final String details;

    public ErrorDetailsResponse(LocalDateTime timestamp, String message, String details) {
        this.timestamp = LocalDateTime.parse(timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        this.message = message;
        this.details = details;
    }
}
