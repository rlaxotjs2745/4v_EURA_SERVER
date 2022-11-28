package com.eura.web.base;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.eura.web.model.UserMapper;
import com.eura.web.model.DTO.UserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BaseController {
    @Autowired
    private UserMapper userMapper;

    @Value("${srvinfo}")
    public String srvinfo;

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
     * 시간 비교
     * @param _date
     * @return 체크할 시간이 _date 보다 크면 1, 작으면 0
     * @throws ParseException
     */
    public Integer getTimeDiff(String _date, Integer _chkSec) throws ParseException{
        long nowTime = System.currentTimeMillis();
        SimpleDateFormat _ex = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date _tmp = _ex.parse(_date);
        long _chkTime = _tmp.getTime() - (_chkSec * 1000);// _chkSec 초단위 만큼 차감
        System.out.println("nowTime: " + nowTime);
        System.out.println("_chkTime: " + _chkTime);
        if(nowTime >= _chkTime){
            return 1;
        }else{
            return 0;
        }
    }

    /**
     * 일시 비교
     * @param _date
     * @param _date2
     * @return Integer 0:동일, 0>:이전, 0<:이후
     * @throws ParseException
     */
    public Integer getDateTimeDiff(String _date1, String _date2) throws ParseException{
        SimpleDateFormat _ex = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = _ex.parse(_date1);
        Date date2 = _ex.parse(_date2);
        return date1.compareTo(date2);
    }

    public Integer getDateTimeDiff(String _date1, Date _date2) throws ParseException{
        SimpleDateFormat _ex = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = _ex.parse(_date1);
        return date1.compareTo(_date2);
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
        if(srvinfo.equals("dev")){
            String a = req.getHeader("uid");
            if(a != null || StringUtils.isNotEmpty(a)){
                rs = userMapper.getUserInfoById(a);
            }
            if(rs == null){
                if(req.getCookies() != null){
                    Cookie o[] = req.getCookies();
                    if(o!=null){
                        for (Cookie c : o) {
                            if(c.getName().equals("user_id")){
                                if(!c.getValue().isEmpty()){
                                    rs = userMapper.getUserInfoById(c.getValue());
                                }
                            }
                        }
                    }
                }
            }
        }else{
            if(req.getCookies() != null){
                Cookie o[] = req.getCookies();
                if(o!=null){
                    for (Cookie c : o) {
                        if(c.getName().equals("user_id")){
                            if(!c.getValue().isEmpty()){
                                rs = userMapper.getUserInfoById(c.getValue());
                            }
                        }
                    }
                }
            }
        }
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
        if(srvinfo.equals("dev")){
            String a = req.getHeader("uid");
            System.out.println(req.getHeader("uid"));
            if(a == null){
                if(req.getCookies() != null){
                    Cookie o[] = req.getCookies();
                    if(o!=null){
                        for (Cookie c : o) {
                            if(c.getName().equals("user_id")){
                                if(!c.getValue().isEmpty()){
                                    rs = c.getValue();
                                }
                            }
                        }
                    }
                }
            }else{
                if(a != null || !a.equals("") || !a.isEmpty()){
                    rs = req.getHeader("uid");
                }
                if(rs == ""){
                    if(req.getCookies() != null){
                        Cookie o[] = req.getCookies();
                        if(o!=null){
                            for (Cookie c : o) {
                                if(c.getName().equals("user_id")){
                                    if(!c.getValue().isEmpty()){
                                        rs = c.getValue();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else{
            if(req.getCookies() != null){
                Cookie o[] = req.getCookies();
                if(o!=null){
                    for (Cookie c : o) {
                        if(c.getName().equals("user_id")){
                            if(!c.getValue().isEmpty()){
                                rs = c.getValue();
                            }
                        }
                    }
                }
            }
        }
        return rs;
    }

    /**
     * Second to Time String
     * @param sec
     * @return
     * @throws Exception
     */
    public String getSec2Time(Integer sec) throws Exception {
        int hour = (int)sec/(60*60);
        int minute = (int) (sec-(hour*3600))/60;
        int second = (int) (sec-(minute*60))%60;
        String _hour = "";
        String _minute = "";
        String _second = "";
        if(String.valueOf(hour).length()==1){
            _hour = "0"+hour;
        }else{
            _hour = String.valueOf(hour);
        }
        if(String.valueOf(minute).length()==1){
            _minute = "0"+minute;
        }else{
            _minute = String.valueOf(minute);
        }
        if(String.valueOf(second).length()==1){
            _second = "0"+second;
        }else{
            _second = String.valueOf(second);
        }
        return _hour + ":" + _minute + ":" + _second;
    }
}
