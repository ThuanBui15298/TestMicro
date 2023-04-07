package com.example.userservice.controller;

import com.example.userservice.config.CustomUserDetails;
import com.example.userservice.dto.ChangePasswordRequest;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.entity.Users;
import com.example.userservice.jwt.JwtTokenUtil;
import com.example.userservice.response.Response;
import com.example.userservice.response.ResponseData;
import com.example.userservice.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static org.springframework.http.HttpStatus.OK;
import static com.example.userservice.response.ResponseDataStatus.ERROR;
import static com.example.userservice.response.ResponseDataStatus.SUCCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthenticationManager authenticationManager;

    private final UsersService usersService;

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
                    .message("Đăng nhập thành công")
                    .data("Token: " + jwt).build(), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.toString())
                    .message("Mã hoặc mật khẩu đăng nhập không đúng").build(), BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>(ResponseData.builder()
                .status(SUCCESS.name())
                .message("Đăng xuất thành công").build(), OK);
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
