package com.eura.web.config.Interceptor;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class ApiAuthorityInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		String ip = getClientIP(req);
		System.out.println(ip);
        Boolean _r = false;
        try {
			if(req.getCookies() != null){
                Cookie o[] = req.getCookies();
                for(int i=0;i<o.length;i++){
                    if(o[i].getName().equals("user_id")){
                        if(!o[i].getValue().isEmpty()){
                            _r = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		if(_r==false){
			Map<String, Object> _resultMap = new HashMap<String, Object>();
			_resultMap.put("success", "0");
			_resultMap.put("code", "-1003");
			_resultMap.put("msg", "A resource that can not be accessed with the privileges it has.");
			res.getWriter().write(new Gson().toJson(_resultMap));
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			res.setStatus(400);
		}
		return _r;
	}

	protected String getClientIP(HttpServletRequest req) {
		String ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null || ip.length() == 0) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = req.getHeader("WL-Proxy-Client-IP"); // ???
		}
		if (ip == null || ip.length() == 0) {
			ip = req.getRemoteAddr();
		}
		return ip;
	}
}
