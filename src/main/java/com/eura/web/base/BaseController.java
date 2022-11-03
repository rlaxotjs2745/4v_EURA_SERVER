package com.eura.web.base;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
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
                if(browser.equals("Safari") || browser.equals("Firefox")) reFileNm = URLDecoder.decode(reFileNm);
            }
        } catch(Exception e){} return reFileNm;
    }

    /**
	 * Client IP 조회
	 * @param request
	 * @return
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
     * @return
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
     * @return
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

        LocalDate date = LocalDate.of(Calendar.YEAR, Calendar.MONTH, Calendar.DATE);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        _r = dayOfWeek.getValue();
        return _r;
    }
}
