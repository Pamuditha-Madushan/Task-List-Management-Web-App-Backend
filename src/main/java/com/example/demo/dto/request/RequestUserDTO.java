package com.example.demo.dto.request;

import com.example.demo.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestUserDTO implements SuperDTO {

    private String username;

    private String password;


}
