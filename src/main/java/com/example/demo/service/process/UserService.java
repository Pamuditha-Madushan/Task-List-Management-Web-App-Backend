package com.example.demo.service.process;

import com.example.demo.dto.request.RequestUserDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UserService {

    public CommonResponseDTO registerUser(RequestUserDTO requestUserDTO) throws IOException;
}
