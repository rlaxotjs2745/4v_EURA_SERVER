package com.eura.web.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.eura.web.model.DTO.MeetingVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Date;

import java.io.UnsupportedEncodingException;
// import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        System.out.println("JWT");
    }
        
    /**
     * 토큰 생성
     * @param userPk
     * @param _ulvl
     * @param _data
     * @return token = jwt
     */
    public String createToken(String userPk, Long _sDt, Long _eDt, String __eDt, Map<String, Object> _data, String roles) {
        //Header 부분 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //payload 부분 설정
        // Map<String, Object> payloads = new HashMap<>();
        Claims payloads = Jwts.claims().setSubject(userPk);
        payloads.put("app_key", "IXRnxaqW54HA3gI6dZ0gkzp1MxhUfm4tHaHm");
        payloads.put("version", "1");
        payloads.put("role_type", roles);
        payloads.put("iat", _sDt);
        payloads.put("exp", _eDt);
        payloads.put("tpc", userPk);
        payloads.put("userid", _data.get("userid"));
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date ext = new Date(); // 토큰 만료 시간
        String jwt = "";
        try {
            ext = dateFormat.parse(__eDt);
            System.out.println(ext);
            ext.setTime(ext.getTime() + expiredTime);

            // 토큰 Builder
            jwt = Jwts.builder()
                .setHeader(headers) // Headers 설정
                .setClaims(payloads) // Claims 설정
                .setSubject(userPk) // 토큰 용도 
                .setExpiration(ext) // 토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes()) // HS256과 Key로 Sign
                .compact(); // 토큰 생성
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jwt;
    }
    
    /**
     * 토큰 검증
     * @param token
     * @return Map
     */
    public Map<String, Object> verifyJWT(String token) {
        Map<String, Object> claimMap = null;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes()) // Set Key
                    .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                    .getBody();

            claimMap = claims;

            //Date expiration = claims.get("exp", Date.class);
            //String data = claims.get("data", String.class);
            
        } catch (ExpiredJwtException e) { // 토큰이 만료되었을 경우
            System.out.println("token expired");
        } catch (Exception e) { // 그외 에러났을 경우
            System.out.println("token verify error");
        }
        return claimMap;
    }

    /**
     * token 파싱
     * * Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
     * @param req
     * @return X-AUTH-TOKEN token String
     */
    public String resolveToken(HttpServletRequest req) {
        String a = req.getHeader("X-AUTH-TOKEN");
        if(a==null || "".equals(a)){
            if(req.getCookies() != null){
                Cookie o[] = req.getCookies();
                for(int i=0;i<o.length;i++){
                    if(o[i].getName().equals("token")){
                        a = o[i].getValue();
                    }
                }
            }
        }
        return a;
    }

    /**
     * data 파싱
     * @param token
     * @return UserVO
     */
    public MeetingVO getRoles(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
        ObjectMapper a = new ObjectMapper();
        MeetingVO b = a.convertValue(claims.get("data"), MeetingVO.class);
        return b;
    }

    /**
     * Claims에 저장된 데이터 뽑기
     * @param token
     * @param target
     * @return String
     */
    public String getClaims(String token, String target) {
        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
        return String.valueOf(claims.get(target));
    }
}
