package com.example.demo.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class StandardResponse {
    private int code;
    private String message;
    private Object data;
}
