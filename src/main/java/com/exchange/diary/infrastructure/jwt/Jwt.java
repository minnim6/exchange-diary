package com.exchange.diary.infrastructure.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;

@NoArgsConstructor
@Getter
public class Jwt {

    @NoArgsConstructor
    @Getter
    public static class Request{
        private String accessToken;
        private String refreshToken;
    }

    @NoArgsConstructor
    @Getter
    public static class Response{
        private String accessToken;
        private String refreshToken;
        private Date accessTokenExpireTime;

        @Builder
        public Response(String accessToken,String refreshToken,Date accessTokenExpireTime){
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.accessTokenExpireTime = accessTokenExpireTime;
        }
    }

    @RequiredArgsConstructor
    @Getter
    public static class ResponseAccessToken{
        private final String accessToken;
        private final Date accessTokenExpireTime;
    }
}
