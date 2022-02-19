package com.exchange.diary.application;

import com.exchange.diary.infrastructure.jwt.Jwt;
import com.exchange.diary.infrastructure.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class JwtController {

    private final JwtUtil jwtUtil;

    @PostMapping("/jwt")
    public Jwt.ResponseAccessToken RequestAccessTokenΩ(@RequestBody Jwt.Request request){
        return jwtUtil.requestAccessToken(request);
    }

}
