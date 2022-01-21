package com.exchange.diary.infrastructure.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    @Value("${jwt.secret_key}")
    private String secretKey;

    private final String AUTHORITIES_KEY = "memberNickname";

    private final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

    private final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;

    @PostConstruct
    protected void settingsToken() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Jwt.Response createJwt(Long memberNumber){
        Jwt.ResponseAccessToken accessToken = createAccessToken(memberNumber);
        return Jwt.Response.builder()
                .accessToken(accessToken.getAccessToken())
                .accessTokenExpireTime(accessToken.getAccessTokenExpireTime())
                .refreshToken(createRefreshToken())
                .build();
    }

    public Jwt.ResponseAccessToken createAccessToken(Long memberNumber){
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        String accessToken = Jwts.builder()
                .claim(AUTHORITIES_KEY,memberNumber.toString())
                .setExpiration(accessTokenExpireTime) //만료시간
                .signWith(SignatureAlgorithm.HS256,secretKey) //인코딩 방식
                .compact();
        return new Jwt.ResponseAccessToken(accessToken,accessTokenExpireTime);
    }

    public String createRefreshToken(){
        return Jwts.builder()
                .setExpiration(createRefreshTokenExpireTime()) //만료시간
                .signWith(SignatureAlgorithm.HS256,secretKey) //인코딩 방식
                .compact();
    }

    public Date createRefreshTokenExpireTime(){
        return new Date(new Date().getTime()+REFRESH_TOKEN_EXPIRE_TIME);
    }

    public Date createAccessTokenExpireTime(){
        return new Date(new Date().getTime()+ACCESS_TOKEN_EXPIRE_TIME);
    }

}
