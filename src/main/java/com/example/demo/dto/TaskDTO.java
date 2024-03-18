package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TaskDTO implements SuperDTO {

    private String title;

    private String description;

    private String status;

    private LocalDateTime timestamp;
}
