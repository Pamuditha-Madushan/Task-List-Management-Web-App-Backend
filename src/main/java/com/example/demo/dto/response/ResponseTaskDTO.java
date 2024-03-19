package com.example.demo.dto.response;

import com.example.demo.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseTaskDTO implements SuperDTO {

    private Long id;

    private String title;

    private String description;

    private String status;

    private LocalDateTime timestamp;


}
