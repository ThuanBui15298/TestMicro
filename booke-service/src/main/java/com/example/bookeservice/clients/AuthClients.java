package com.example.bookeservice.clients;

import com.example.bookeservice.config.FeignClientInterceptor;
import com.example.bookeservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", configuration = FeignClientInterceptor.class)
public interface AuthClients {

    @GetMapping("/users")
    UserDTO getUser();

    @GetMapping("users/detail")
    UserDTO getUserById(@RequestParam Long id);
}
