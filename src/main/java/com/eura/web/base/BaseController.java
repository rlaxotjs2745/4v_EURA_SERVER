package com.eura.web.base;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.eura.web.model.UserMapper;
import com.eura.web.model.DTO.UserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BaseController {
    @Autowired
    private UserMapper userMapper;

    public String getBrowser(HttpServletRequest req) {
        String userAgent = req.getHeader("User-Agent");
        if(userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1 /*IE11*/|| userAgent.indexOf("Edge") > -1) {
            return "MSIE";
        } else if(userAgent.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if(userAgent.indexOf("Opera") > -1) {
            return "Opera";
        } else if(userAgent.indexOf("Safari") > -1) {
            return "Safari";
        } else if(userAgent.indexOf("Firefox") > -1){
            return "Firefox";
        } else{
            return null;
        }
    }

    public String getFileNm(String browser, String fileNm){
        String reFileNm = null;
        try {
            if (browser.equals("MSIE") || browser.equals("Trident") || browser.equals("Edge")) {
                reFileNm = URLEncoder.encode(fileNm, "UTF-8").replaceAll("\\+", "%20");
            } else {
                if(browser.equals("Chrome")){
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < fileNm.length(); i++) {
                        char c = fileNm.charAt(i);
                        if (c > '~') {
                            sb.append(URLEncoder.encode(Character.toString(c), "UTF-8"));
                        } else {
                            sb.append(c);
                        }
                    }
                    reFileNm = sb.toString();
                } else{
                    reFileNm = new String(fileNm.getBytes("UTF-8"), "ISO-8859-1");
                }
                if(browser.equals("Safari") || browser.equals("Firefox")) reFileNm = URLDecoder.decode(reFileNm, "UTF-8");
            }
        } catch(Exception e){} return reFileNm;
    }

    /**
	 * Client IP 조회
	 * @param request
	 * @return String
	 */
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

    /**
     * 년월일 날짜 계산
     * @param _date
     * @param _y
     * @param _m
     * @param _d
     * @return String
     */
    public String getCalDate(String _date, Integer _y, Integer _m, Integer _d){
        String _r = "";
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            cal.setTime(df.parse(_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(_y!=0){cal.add(Calendar.YEAR, _y);}
        if(_m!=0){cal.add(Calendar.MONTH, _m);}
        if(_d!=0){cal.add(Calendar.DATE, _d);}
        _r = df.format(cal.getTime());
        return _r;
    }

    /**
     * 요일 구하기
     * @param _date
     * @param _y
     * @param _m
     * @param _d
     * @return Integer
     */
    public Integer getCalDayOfWeek(String _date, Integer _y, Integer _m, Integer _d){
        Integer _r = 0;
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(df.parse(_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(_y!=0){cal.add(Calendar.YEAR, _y);}
        if(_m!=0){cal.add(Calendar.MONTH, _m);}
        if(_d!=0){cal.add(Calendar.DATE, _d);}

        _r = cal.get(Calendar.DAY_OF_WEEK);
        return _r;
    }

    /**
     * 날짜 비교
     * @param _date1
     * @param _date2
     * @return 0:동일, 0>:이전, 0<:이후
     * @throws ParseException
     */
    public Integer getDateDiff(String _date1, String _date2) throws ParseException{
        SimpleDateFormat _ex = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = _ex.parse(_date1);
        Date date2 = _ex.parse(_date2);
        return date1.compareTo(date2);
    }

    /**
     * get Map Object from JSON String by GSON
     * @param jsonStr
     * @return List<Map<String, String>>
     */
    public static List<Map<String, String>> getListFromJson(String jsonStr) {
		return new Gson().fromJson(jsonStr, new TypeToken<List<Map<String, String>>>() {
        }.getType());
	}

    /**
     * 이메일 to UserVO
     * @param req
     * @return UserVO
     * @throws Exception
     */
    public UserVO getChkUserLogin(HttpServletRequest req) throws Exception{
        UserVO rs = new UserVO();
        String a = req.getHeader("auth");
        if(!a.equals("") || a!=null){
            rs = userMapper.getUserInfoById(a);
        }
        // if(req.getCookies() != null){
        //     Cookie o[] = req.getCookies();
        //     if(o!=null){
        //         for (Cookie c : o) {
        //             if(c.getName().equals("user_id")){
        //                 if(!c.getValue().isEmpty()){
        //                     rs = userMapper.getUserInfoById(c.getValue());
        //                 }
        //             }
        //         }
        //     }
        // }
        return rs;
    }

    /**
     * 쿠키에서 아이디 가져오기
     * @param req
     * @return String
     * @throws Exception
     */
    public String getUserID(HttpServletRequest req) throws Exception{
        String rs = "";
        String a = req.getHeader("auth");
        if(!a.equals("") || a!=null){
            rs = a;
        }
        // if(req.getCookies() != null){
        //     Cookie o[] = req.getCookies();
        //     if(o!=null){
        //         for (Cookie c : o) {
        //             if(c.getName().equals("user_id")){
        //                 if(!c.getValue().isEmpty()){
        //                     rs = c.getValue();
        //                 }
        //             }
        //         }
        //     }
        // }
        return rs;
    }
}
