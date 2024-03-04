package com.example.demo.dto.request;

import com.example.demo.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginUserDTO implements SuperDTO {

    @NotBlank(message = "Username cannot be empty !")
    @Size(min = 6, max = 30, message = "Username must be between 6 to 30 characters !")
    private String username;

    @NotBlank(message = "Password cannot be empty !")
    @Size(min = 6, message = "Password must be at least 6 characters long !")
    private String password;

}
