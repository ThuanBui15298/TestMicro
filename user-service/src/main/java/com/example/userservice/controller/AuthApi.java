package com.example.userservice.controller;

import com.example.userservice.config.CustomUserDetails;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.jwt.JwtTokenUtil;
import com.example.userservice.response.ResponseData;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.example.userservice.response.ResponseDataStatus.ERROR;
import static com.example.userservice.response.ResponseDataStatus.SUCCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getCode(), loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenUtil.generateToken((CustomUserDetails) authentication.getPrincipal());
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("User signed-in successfully!")
                    .data(jwt).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name())
                    .message("Code or password failes").build(), BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Đăng xuất thành công").build(), OK);
    }

}
