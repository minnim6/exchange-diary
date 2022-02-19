package com.exchange.diary.infrastructure.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    @Value("${jwt.secret_key}")
    private String secretKey;

    private final String AUTHORITIES_KEY = "memberNumber";

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
                .refreshToken(createRefreshToken(memberNumber))
                .build();
    }

    public boolean isValidateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); // 가져온 토큰을 claims
            return !claims.getBody().getExpiration().before(new Date()); // 만료된 토큰일 경우 false 리턴
        } catch (JwtException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = paresClaims(token);
        if (!isCheckAuthorities(claims)) {
            //TODO 커스텀 에러처리
        }

        Long memberNumber =  Long.valueOf(String.valueOf(claims.get(AUTHORITIES_KEY)));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
        // 상수화!!
        UserDetails userDetails = new User((String) claims.get(AUTHORITIES_KEY), Strings.EMPTY, authorities);

        return new UsernamePasswordAuthenticationToken(userDetails, Strings.EMPTY, authorities);
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

    public Jwt.ResponseAccessToken requestAccessToken(Jwt.Request request){
        if(isValidateToken(request.getRefreshToken())&&isValidateMemberNumber(request)){
            return createAccessToken(getMemberNumber(request.getRefreshToken()));
        }
        //TODO 에러처리 추가
        throw new NullPointerException();
    }


    private String createRefreshToken(Long memberNumber){
        return Jwts.builder()
                .claim(AUTHORITIES_KEY,memberNumber.toString())
                .setExpiration(createRefreshTokenExpireTime()) //만료시간
                .signWith(SignatureAlgorithm.HS256,secretKey) //인코딩 방식
                .compact();
    }

    private Claims paresClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            //TODO 커스텀 예외 처리 추가
            throw new JwtException("올바르지 않은 토큰입니다.", e);
        }
    }

    private boolean isValidateMemberNumber(Jwt.Request request){
        return paresClaims(request.getRefreshToken()).get(AUTHORITIES_KEY)
                .equals(paresClaims(request.getAccessToken()).get(AUTHORITIES_KEY));
    }

    private Long getMemberNumber(String token){
        return Long.parseLong((String) paresClaims(token).get(AUTHORITIES_KEY));
    }

    private boolean isCheckAuthorities(Claims claims) {
        return !(claims.get(AUTHORITIES_KEY) == null);
    }

    private Date createRefreshTokenExpireTime(){
        return new Date(new Date().getTime()+REFRESH_TOKEN_EXPIRE_TIME);
    }

    private Date createAccessTokenExpireTime(){
        return new Date(new Date().getTime()+ACCESS_TOKEN_EXPIRE_TIME);
    }

}
