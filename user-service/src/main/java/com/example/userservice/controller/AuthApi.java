package com.example.userservice.controller;

import com.example.userservice.config.CustomUserDetails;
import com.example.userservice.dto.ChangePasswordRequest;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.entity.Users;
import com.example.userservice.jwt.JwtTokenUtil;
import com.example.userservice.response.Response;
import com.example.userservice.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthenticationManager authenticationManager;

    private final UsersService usersService;

    @PostMapping("/login")
    public ResponseEntity<Response<String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getCode(),loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenUtil.generateToken((CustomUserDetails) authentication.getPrincipal());
            return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(), "OK", jwt, 1L));

        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(),"Mã hoặc mật khẩu đăng nhập không đúng" ));

        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>("Đăng xuất thành công", HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        Users user = usersService.findByUserCode(changePasswordRequest.getUserCode());

        if (!usersService.checkIfValidOldPassword(user, changePasswordRequest.getOldPassword())) {
            return ResponseEntity.ok().body(new Response<>(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Mật khẩu cũ không đúng!"));
        }

        if (user.getCode().equals(changePasswordRequest.getUserCode())) {
            usersService.changeAccountPassword(user, changePasswordRequest.getNewPassword());
            return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(), "Đổi mật khẩu thành công"));
        } else {
            throw new UsernameNotFoundException("Can not found for code: " + changePasswordRequest.getUserCode());
        }
    }
}
