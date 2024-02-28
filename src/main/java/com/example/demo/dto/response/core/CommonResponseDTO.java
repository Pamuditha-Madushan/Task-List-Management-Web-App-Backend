package com.example.demo.dto.response.core;

import com.example.demo.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CommonResponseDTO implements SuperDTO {

    private int code;
    private String message;
    private Object data;
    private ArrayList<Object> records;
}
