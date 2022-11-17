package com.eura.web.config.Interceptor;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eura.web.model.UserMapper;
import com.eura.web.model.DTO.UserVO;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class ApiAuthorityInterceptor implements HandlerInterceptor {
    @Autowired
    private final UserMapper userMapper;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		// String ip = getClientIP(req);
		// System.out.println(ip);
        Boolean _r = false;
        try {
			String a = req.getHeader("uid");
			System.out.println(req.getHeader("uid"));
			if(a == null){
				if(req.getCookies() != null){
					Cookie o[] = req.getCookies();
					if(o!=null){
						for (Cookie c : o) {
							if(c.getName().equals("user_id")){
								if(!c.getValue().isEmpty()){
									UserVO rs = userMapper.getUserInfoById(c.getValue());
									if(rs!=null){
										_r = true;
									}
								}
							}
						}
					}
				}
			}else{
				if(!a.equals("") || !a.isEmpty() || a != null){
					UserVO rs = userMapper.getUserInfoById(a);
					if(rs!=null){
						_r = true;
					}
				}
				if(!_r){
					if(req.getCookies() != null){
						Cookie o[] = req.getCookies();
						if(o!=null){
							for (Cookie c : o) {
								if(c.getName().equals("user_id")){
									if(!c.getValue().isEmpty()){
										UserVO rs = userMapper.getUserInfoById(c.getValue());
										if(rs!=null){
											_r = true;
										}
									}
								}
							}
						}
					}
				}
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
		if(_r==false){
			Map<String, Object> _resultMap = new HashMap<String, Object>();
			_resultMap.put("result_code", "FAIL");
			_resultMap.put("result_str", "로그인 후에 이용해주세요..");
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			res.setStatus(420);
			res.getWriter().write(new Gson().toJson(_resultMap));
		}
		return _r;
	}

	protected String getClientIP(HttpServletRequest req) {
		String ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null || ip.length() == 0) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = req.getHeader("WL-Proxy-Client-IP"); // 웹로직
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("HTTP_CLIENT_IP");
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = req.getHeader("X-Real-IP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = req.getHeader("X-RealIP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = req.getHeader("REMOTE_ADDR");
        }
		if (ip == null || ip.length() == 0) {
			ip = req.getRemoteAddr();
		}
		return ip;
	}
}
