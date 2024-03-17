package com.example.demo.api;

import com.example.demo.dto.request.LoginUserDTO;
import com.example.demo.dto.request.RequestUserDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import com.example.demo.exception.SQLIntegrityConstraintViolationException;
import com.example.demo.exception.UnAuthorizedException;
import com.example.demo.service.process.UserService;
import com.example.demo.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    @PostMapping(path = {"/register"})
    public ResponseEntity<StandardResponse> registerUser(
            @Valid @RequestBody RequestUserDTO requestUserDTO) throws IOException {
            CommonResponseDTO registeredStateData = userService.registerUser(requestUserDTO);

            return new ResponseEntity(new StandardResponse(registeredStateData.getCode(),
                    registeredStateData.getMessage(), registeredStateData.getData()),
                    registeredStateData.getCode() == 201 ? HttpStatus.CREATED :
                            registeredStateData.getCode() == 409 ? HttpStatus.CONFLICT : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = {"/login"})
    public ResponseEntity<StandardResponse> loginUser(
            @Valid @RequestBody LoginUserDTO loginUserDTO, HttpServletResponse response) throws IOException {
        try {
            CommonResponseDTO loggedInStateData = userService.loginUser(loginUserDTO, response);
            return new ResponseEntity(new StandardResponse(loggedInStateData.getCode(),
                    loggedInStateData.getMessage(), loggedInStateData.getData()),
                    loggedInStateData.getCode() == 200 ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnAuthorizedException e) {
            throw e;
        } catch (UsernameNotFoundException ex) {
            throw ex;
        }
    }
}
