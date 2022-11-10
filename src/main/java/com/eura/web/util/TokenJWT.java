package com.eura.web.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.Base64;
import java.util.Date;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class TokenJWT {

    @Value("${spring.jwt.secret}")
    private String secretKey;
    final Long expiredTime = 1000 * 60L * 60L * 2L; // 토큰 유효 시간 (2시간)

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public void main(String[] args) throws UnsupportedEncodingException {
        // System.out.println("JWT");
    }
        
    /**
     * 토큰 생성
     * @param userPk
     * @param _ulvl
     * @param _data
     * @return token = jwt
     */
    public String createToken(String userPk, Long _sDt, Long _eDt, String __eDt, Map<String, Object> _data, Integer roles) {
        //Header 부분 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //payload 부분 설정
        Claims payloads = Jwts.claims();
        payloads.put("app_key", "2xRuGTAKpBDRcmUrNDkAAbr0KgaMUr1Loq1b");
        payloads.put("version", 1);
        payloads.put("role_type", roles);
        payloads.put("iat", _sDt);
        payloads.put("exp", (_eDt + expiredTime));
        payloads.put("tpc", userPk);
        payloads.put("userid", _data.get("userid"));
        
        Date ext = new Date(); // 토큰 만료 시간
        String jwt = "";

        ext.setTime(_eDt + expiredTime);

        // 토큰 Builder
        jwt = Jwts.builder()
            .setHeader(headers) // Headers 설정
            .setClaims(payloads) // Claims 설정
            .setExpiration(ext) // 토큰 만료 시간 설정
            .signWith(SignatureAlgorithm.HS256, secretKey) // HS256과 Key로 Sign
            .compact(); // 토큰 생성

        return jwt;
    }
}
